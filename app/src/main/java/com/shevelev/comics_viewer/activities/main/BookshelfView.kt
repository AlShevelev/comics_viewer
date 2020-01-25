package com.shevelev.comics_viewer.activities.main

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsInfo
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfListAdapter
import com.shevelev.comics_viewer.common.custom_controls.ProgressBar
import com.shevelev.comics_viewer.common.structs.Size
import com.shevelev.comics_viewer.utils.indexOfFirstOrNull

/**
 * Main view for bookshelf
 */
class BookshelfView(activity: Activity?, layoutId: Int, private val changeModeHandler: IChangeModeHandlerView, // On comics click handler - for view comics
                    private val onComicsChoosen: (Long) -> Unit) : LinearLayout(activity) {
    private val shelfsList // List with shelfes
        : ListView
    private val progressBar: ProgressBar
    private var books: List<BookshelfComicsInfo>? = null
    private var adapter: BookshelfListAdapter? = null
    private var parentSize: Size? = null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentSize = Size(w, h)
        if (books != null) initListByBooks(onComicsChoosen) // Create adapter here
    }

    fun showProgress() {
        progressBar.show() // this execute BEFORE comics creation
    }

    fun hideProgress() {
        progressBar.hide() // this execute after comics creation
    }

    fun setProgressText(text: String?) {
        progressBar.setText(text)
    }

    /**
     * Place books in bookshelfs
     * @param books lis of comics
     */
    fun setBooks(books: List<BookshelfComicsInfo>?) {
        this.books = books
        if (parentSize != null) initListByBooks(onComicsChoosen) // Create adapter here
    }

    private fun initListByBooks(onComicsChoosen: (Long) -> Unit) {
        adapter = BookshelfListAdapter((context as Activity), books!!, changeModeHandler, onComicsChoosen)
        adapter!!.setParentSize(parentSize!!)
        shelfsList.adapter = adapter
    }

    /**
     * Scroll list of books to end
     * @param idOfComicsToScroll - id of comics to scroll to (if null - without scrolling)
     */
    fun scrollToComics(idOfComicsToScroll: Long?) {
        if (books != null && adapter != null && idOfComicsToScroll != null && shelfsList.canScrollVertically(1)) {
            val indexOfComics = books.indexOfFirstOrNull { item: BookshelfComicsInfo -> item.id == idOfComicsToScroll }

            if (indexOfComics != null) {
                val indexOfShelf = adapter!!.getShelfForComicsIndex(indexOfComics)
                val shelfsOnScreen = adapter!!.shelfsOnScreenCount
                if (indexOfShelf <= shelfsOnScreen - 1) // Don't need in scroling for first rows
                    return
                shelfsList.setSelection(indexOfShelf)
            }
        }
    }

    init {
        View.inflate(activity, layoutId, this)
        progressBar = findViewById<View>(R.id.mainActivityProgressBar) as ProgressBar
        shelfsList = findViewById<View>(R.id.lvShelfs) as ListView
    }
}