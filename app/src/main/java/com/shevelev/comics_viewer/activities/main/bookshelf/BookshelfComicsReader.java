package com.shevelev.comics_viewer.activities.main.bookshelf;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.comics_filters.BookcaseComics;
import com.shevelev.comics_viewer.activities.main.comics_filters.IComicsFilter;
import com.shevelev.comics_viewer.comics_workers.CoverCreator;
import com.shevelev.comics_viewer.comics_workers.IPreviewCreator;
import com.shevelev.comics_viewer.comics_workers.PreviewCreator;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs;
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper;
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper;
import com.shevelev.comics_viewer.common.structs.Size;

import java.util.List;

/**
 * Read comics for bookshelf
 */
public class BookshelfComicsReader  extends AsyncTask<Void, Void, Void>
{
    private List<BookshelfComicsInfo> readComics;

    private IActionZeroArgs beforeExecute;
    private IActionOneArgs<List<BookshelfComicsInfo>> afterExecute;

    private IComicsFilter comicsFilter;     // source of comics

    private Bitmap privateCover;

    private final IPreviewCreator previewCreator;

    public BookshelfComicsReader(
            IComicsFilter comicsFilter,
            IActionZeroArgs beforeExecute,
            IActionOneArgs<List<BookshelfComicsInfo>> afterExecute,
            Size clientSize)
    {
        this.beforeExecute = beforeExecute;
        this.afterExecute = afterExecute;
        this.comicsFilter = comicsFilter;

        previewCreator = new PreviewCreator(clientSize);
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            List<BookcaseComics> comics=comicsFilter.getComics();

            if(comics!=null)
                readComics=CollectionsHelper.transform(comics, item ->
                        new BookshelfComicsInfo(
                                item.id,
                                item.displayName,
                                getCover(item),
                                item.isNeedShowPrivateCover));

            else
                readComics = null;
        }
        catch (Exception ex)
        {
            Log.e("CV", "exception", ex);
        }

        return null;
    }

    private Bitmap getCover(BookcaseComics comics)
    {
        if(comics.isNeedShowPrivateCover)
        {
            if(privateCover==null)
                privateCover = CoverCreator.create(BitmapsHelper.loadFromRaw(R.drawable.img_private_comics_cover), previewCreator);

            return privateCover;
        }

        return BitmapsHelper.loadFromFile(AppPrivateFilesHelper.getFullName(comics.coverFilename));
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        beforeExecute.process();
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);

        afterExecute.process(readComics);
    }
}