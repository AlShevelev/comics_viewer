package com.shevelev.comics_viewer.activities.main.bookshelf

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.IChangeModeHandlerView
import com.shevelev.comics_viewer.activities.main.bookshelf.ViewHolders.ShelfHolder
import com.shevelev.comics_viewer.activities.main.bookshelf.comics_control.ComicsControl
import com.shevelev.comics_viewer.common.structs.Size
import java.util.*

/**
 * Adaptor for displaing one bookshelf
 */
class BookshelfListAdapter(
    private val activity: Activity,
    private val books: List<BookshelfComicsInfo>,
    private val changeModeHandlerView: IChangeModeHandlerView,
    private val onComicsChoosen: (Long) -> Unit       // On comics click handler - for view comics
) : BaseAdapter() {
    private var booksCount : Int = 0        // Books total
    private var booksOnShelf = 0 // Max books on shelf = 0

    /**
     * Get quantity of shelfs on screen
     */
    var shelfsOnScreenCount = 0
        private set

    private var parentSize // Size of content view
        : Size? = null
    private var maxComicsWidth = 0

    init {
        booksCount = books.size
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun setParentSize(parentSize: Size) {
        this.parentSize = parentSize
        if (parentSize.width < parentSize.height) {
            booksOnShelf = BOOKS_ON_SHELF_VERT
            shelfsOnScreenCount = SHELF_COUNT_MIN_VERT
        } else {
            booksOnShelf = BOOKS_ON_SHELF_HORIZ
            shelfsOnScreenCount = SHELF_COUNT_MIN_HORIZ
        }
        maxComicsWidth = parentSize.width / booksOnShelf
    }

    /**
     * Get total items count
     */
    override fun getCount(): Int {
        var result = 0
        if (booksCount <= booksOnShelf * shelfsOnScreenCount) result = shelfsOnScreenCount // Alwais show at least shelfCountMin shelfs + header of bookcase
        else {
            result = booksCount / booksOnShelf
            if (booksCount % booksOnShelf != 0) // Not full shelf
                result++
        }
        return result + 1 // + header of bookcase
    }

    /**
     * To prevent white splash when click on shelf
     * @param position
     * @return
     */
    override fun isEnabled(position: Int): Boolean {
        return false
    }

    override fun getItem(position: Int): Any {
        return position
    }

    /**
     * How many layout types we have got
     */
    override fun getViewTypeCount(): Int {
        return 2 // Two kind of layout
    }

    /**
     * Returns dependence type of layout from position
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
        if (position == 0) createHeaderView(convertView) else createBookshelfView(position, convertView)

    private fun createHeaderView(convertView: View?): View {
        var resultView = convertView

        if (resultView == null) {
            resultView = inflater!!.inflate(R.layout.bookshelf_header, null)
            val allComicsControl = resultView.findViewById<View>(R.id.allComicsText) as TextView
            val recentComicsControl = resultView.findViewById<View>(R.id.recentText) as TextView
            allComicsControl.setOnClickListener { v: View? -> changeModeHandlerView.onAllComicsClicked(allComicsControl, recentComicsControl) }
            recentComicsControl.setOnClickListener { v: View? -> changeModeHandlerView.onRecentComicsClicked(allComicsControl, recentComicsControl) }
            changeModeHandlerView.initState(allComicsControl, recentComicsControl)
        }
        return resultView!!
    }

    private fun createBookshelfView(position: Int, convertView: View?): View {
        var resultView = convertView
        var shelfHolder: ShelfHolder? = null

        if (resultView == null) {
            resultView = inflater!!.inflate(R.layout.bookshelf_list_item, null)
            val p = AbsListView.LayoutParams(parentSize!!.width, parentSize!!.height / shelfsOnScreenCount)
            resultView.layoutParams = p
            shelfHolder = ShelfHolder()
            shelfHolder.loverShelf = resultView.findViewById<View>(R.id.ivShelf) as ImageView
            shelfHolder.comicsContainer = resultView.findViewById<View>(R.id.comicsContainer) as LinearLayout
            shelfHolder.loverShelf!!.maxWidth = parentSize!!.width
            shelfHolder.loverShelf!!.minimumWidth = parentSize!!.width
            resultView.tag = shelfHolder
        }
        else {
            shelfHolder = resultView.tag as ShelfHolder
        }

        shelfHolder.comicsContainer!!.removeAllViews()
        val comicsOnShelf = getComicsForShelf(position - 1) // -1 because row with index 0 is a header

        for (comicsInfo in comicsOnShelf)  // Create comics for shelves
            shelfHolder.comicsContainer!!.addView(ComicsControl(activity, comicsInfo, maxComicsWidth, onComicsChoosen))

        return resultView!!
    }

    private fun getComicsForShelf(shelfIndex: Int): List<BookshelfComicsInfo> {
        val result = mutableListOf<BookshelfComicsInfo>()
        if (books.isNotEmpty()) {
            var firstBooksIndex = shelfIndex * booksOnShelf
            var count = 0
            while (firstBooksIndex < books.size && count < booksOnShelf) {
                result.add(books[firstBooksIndex])
                firstBooksIndex++
                count++
            }
        }
        return result
    }

    /**
     * Get index of shelf for index of comics
     * @param indexOfComics
     * @return
     */
    fun getShelfForComicsIndex(indexOfComics: Int): Int {
        var indexOfComics = indexOfComics
        if (indexOfComics < booksOnShelf) return 1 // +1 - has header
        indexOfComics++
        val base = indexOfComics / booksOnShelf
        return if (indexOfComics % booksOnShelf != 0) base + 1 else base
    }

    companion object {
        private var inflater: LayoutInflater? = null
        private const val BOOKS_ON_SHELF_VERT = 2
        private const val SHELF_COUNT_MIN_VERT = 3 // Minimum shelfs on screen
        private const val BOOKS_ON_SHELF_HORIZ = 3
        private const val SHELF_COUNT_MIN_HORIZ = 2
    }
}