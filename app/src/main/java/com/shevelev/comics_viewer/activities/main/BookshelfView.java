package com.shevelev.comics_viewer.activities.main;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsInfo;
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfListAdapter;
import com.shevelev.comics_viewer.common.custom_controls.ProgressBar;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.common.structs.Size;

import java.util.List;

/**
 * Main view for bookshelf
 */
public class BookshelfView extends LinearLayout
{
    private ListView shelfsList;           // List with shelfes
    private ProgressBar progressBar;

    private List<BookshelfComicsInfo> books;
    private BookshelfListAdapter adapter;

    private Size parentSize;

    private IActionOneArgs<Long> onComicsChoosen; // On comics click handler - for view comics

    private IChangeModeHandlerView changeModeHandler;

    public BookshelfView(Activity activity, int layoutId, IChangeModeHandlerView changeModeHandler, IActionOneArgs<Long> onComicsChoosen)
    {
        super(activity);

        this.changeModeHandler = changeModeHandler;

        this.onComicsChoosen = onComicsChoosen;

        inflate(activity, layoutId, this);
        progressBar = (ProgressBar)findViewById(R.id.mainActivityProgressBar);

        shelfsList = (ListView) findViewById(R.id.lvShelfs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        parentSize=new Size(w, h);

        if(books != null)
            initListByBooks(onComicsChoosen);              // Create adapter here
    }

    public void showProgress()
    {
        progressBar.show();                // this execute BEFORE comics creation
    }

    public void hideProgress()
    {
        progressBar.hide();              // this execute after comics creation
    }

    public void setProgressText(String text)
    {
        progressBar.setText(text);
    }

    /**
     * Place books in bookshelfs
     * @param books lis of comics
     */
    public void setBooks(List<BookshelfComicsInfo> books)
    {
        this.books = books;

        if(parentSize!=null)
            initListByBooks(onComicsChoosen);            // Create adapter here
    }

    private void initListByBooks(IActionOneArgs<Long> onComicsChoosen)
    {
        adapter = new BookshelfListAdapter((Activity)getContext(), books, changeModeHandler, onComicsChoosen);
        adapter.setParentSize(parentSize);
        shelfsList.setAdapter(adapter);
    }

    /**
     * Scroll list of books to end
     * @param idOfComicsToScroll - id of comics to scroll to (if null - without scrolling)
     */
    public void scrollToComics(Long idOfComicsToScroll)
    {
        if(books!=null && adapter!=null && idOfComicsToScroll!=null && shelfsList.canScrollVertically(1))
        {
            Integer indexOfComics=CollectionsHelper.firstIndexOf(books, (item)->item.getId()==idOfComicsToScroll);
            if(indexOfComics!=null)
            {
                int indexOfShelf=adapter.getShelfForComicsIndex(indexOfComics);
                int shelfsOnScreen = adapter.getShelfsOnScreenCount();
                if(indexOfShelf<=shelfsOnScreen-1)          // Don't need in scroling for first rows
                    return;

                shelfsList.setSelection(indexOfShelf);
            }
        }
    }
}