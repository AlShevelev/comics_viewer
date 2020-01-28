package com.shevelev.comics_viewer.ui.activities.main.bookshelf

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.main.comics_filters.BookcaseComics
import com.shevelev.comics_viewer.ui.activities.main.comics_filters.IComicsFilter
import com.shevelev.comics_viewer.comics_workers.CoverCreator
import com.shevelev.comics_viewer.comics_workers.IPreviewCreator
import com.shevelev.comics_viewer.comics_workers.PreviewCreator
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Read comics for bookshelf
 */
class BookshelfComicsReader(
    // source of comics
    private val context: Context,
    private val comicsFilter: IComicsFilter,
    private val beforeExecute: () -> Unit,
    private val afterExecute: (List<BookshelfComicsInfo>?) -> Unit,
    clientSize: Size) : AsyncTask<Void?, Void?, Void?>() {

    private var readComics: List<BookshelfComicsInfo>? = null
    private var privateCover: Bitmap? = null
    private val previewCreator: IPreviewCreator = PreviewCreator(context, clientSize)

    protected override fun doInBackground(vararg params: Void?): Void? {
        try {
            val comics = comicsFilter.comics
            readComics = if (comics != null) comics.map {
                BookshelfComicsInfo(
                    it.id,
                    it.displayName!!,
                    getCover(it)!!,
                    it.isNeedShowPrivateCover)
            } else {
                null
            }
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
        }
        return null
    }

    private fun getCover(comics: BookcaseComics): Bitmap? {
        if (comics.isNeedShowPrivateCover) {
            if (privateCover == null) privateCover = CoverCreator.create(context, BitmapsHelper.loadFromRaw(context, R.drawable.img_private_comics_cover), previewCreator)
            return privateCover
        }
        return BitmapsHelper.loadFromFile(AppPrivateFilesHelper.getFullName(context, comics.coverFilename!!))
    }

    override fun onPreExecute() {
        super.onPreExecute()
        beforeExecute()
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        afterExecute(readComics)
    }
}