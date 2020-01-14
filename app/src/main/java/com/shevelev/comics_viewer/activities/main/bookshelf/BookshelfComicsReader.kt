package com.shevelev.comics_viewer.activities.main.bookshelf

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.comics_filters.BookcaseComics
import com.shevelev.comics_viewer.activities.main.comics_filters.IComicsFilter
import com.shevelev.comics_viewer.comics_workers.CoverCreator
import com.shevelev.comics_viewer.comics_workers.IPreviewCreator
import com.shevelev.comics_viewer.comics_workers.PreviewCreator
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Read comics for bookshelf
 */
class BookshelfComicsReader(
    // source of comics
    private val comicsFilter: IComicsFilter,
    private val beforeExecute: IActionZeroArgs,
    private val afterExecute: IActionOneArgs<List<BookshelfComicsInfo>?>,
    clientSize: Size) : AsyncTask<Void?, Void?, Void?>() {

    private var readComics: List<BookshelfComicsInfo>? = null
    private var privateCover: Bitmap? = null
    private val previewCreator: IPreviewCreator = PreviewCreator(clientSize)

    protected override fun doInBackground(vararg params: Void?): Void? {
        try {
            val comics = comicsFilter.comics
            readComics = if (comics != null) CollectionsHelper.transform(comics) { item: BookcaseComics ->
                BookshelfComicsInfo(
                    item.id,
                    item.displayName!!,
                    getCover(item)!!,
                    item.isNeedShowPrivateCover)
            } else null
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
        }
        return null
    }

    private fun getCover(comics: BookcaseComics): Bitmap? {
        if (comics.isNeedShowPrivateCover) {
            if (privateCover == null) privateCover = CoverCreator.create(BitmapsHelper.loadFromRaw(R.drawable.img_private_comics_cover), previewCreator)
            return privateCover
        }
        return BitmapsHelper.loadFromFile(AppPrivateFilesHelper.getFullName(comics.coverFilename))
    }

    override fun onPreExecute() {
        super.onPreExecute()
        beforeExecute.process()
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        afterExecute.process(readComics)
    }
}