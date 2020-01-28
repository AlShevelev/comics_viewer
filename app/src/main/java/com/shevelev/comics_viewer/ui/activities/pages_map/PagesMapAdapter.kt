package com.shevelev.comics_viewer.ui.activities.pages_map

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.LinearLayout
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.main.bookshelf.ViewHolders.PagesHolder
import com.shevelev.comics_viewer.common.structs.Size
import com.shevelev.comics_viewer.dal.dto.Page

class PagesMapAdapter(
    private val activity: Activity,
    private val pages: List<Page>,
    activePageIndex: Int,
    private val onChangeActivePage: (Int) -> Unit,
    private val onZoomPage: (Int) -> Unit) : BaseAdapter() {
    // Size of content view
    private var parentSize : Size? = null
    private var pagesInRow = 0 // Max pages in row = 0
    private var rowsCountMin = 0
    private var maxPageWidth = 0
    private val pagesCount: Int = pages.size
    private var activePageIndex: Int
    fun setActivePageIndex(activePageIndex: Int) {
        this.activePageIndex = activePageIndex
    }

    fun setParentSize(parentSize: Size) {
        this.parentSize = parentSize
        if (parentSize.width < parentSize.height) {
            pagesInRow = PAGES_IN_ROW_VERT
            rowsCountMin = ROWS_COUNT_MIN_VERT
        } else {
            pagesInRow = PAGES_IN_ROW_HORIZ
            rowsCountMin = ROWS_COUNT_MIN_HORIZ
        }
        maxPageWidth = parentSize.width / pagesInRow
    }

    override fun getCount(): Int {
        return if (pagesCount <= pagesInRow * rowsCountMin) rowsCountMin // Alwais show at least shelfCountMin shelfs + header of bookcase
        else {
            var result = pagesCount / pagesInRow
            if (pagesCount % pagesInRow != 0) // Not full row
                result++
            result
        }
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        var pageHolder: PagesHolder? = null
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.pages_map_list_item, null)
            val p = AbsListView.LayoutParams(parentSize!!.width, parentSize!!.height / rowsCountMin)
            convertView.layoutParams = p
            pageHolder = PagesHolder()
            pageHolder.pagesControl = convertView.findViewById<View>(R.id.pageContainer) as LinearLayout
            convertView.tag = pageHolder
        } else pageHolder = convertView.tag as PagesHolder
        pageHolder.pagesControl!!.removeAllViews()
        val pagesInRow = getPagesForRow(position)
        var pageNumber = getFirstPageNumberInrow(position)
        for (pageInRow in pagesInRow)  // Create comics for shelfs
        {
            pageHolder!!.pagesControl!!.addView(PageControl(
                activity,
                pageInRow,
                maxPageWidth,
                pageNumber,
                activePageIndex,
                onChangeActivePage,
                onZoomPage))
            pageNumber++
        }
        return convertView
    }

    private fun getPagesForRow(rowIndex: Int): List<Page> {
        val result = mutableListOf<Page>()
        if (pages.size > 0) {
            var firstPagesIndex = rowIndex * pagesInRow
            var count = 0
            while (firstPagesIndex < pages.size && count < pagesInRow) {
                result.add(pages[firstPagesIndex])
                firstPagesIndex++
                count++
            }
        }
        return result
    }

    private fun getFirstPageNumberInrow(rowIndex: Int): Int {
        return rowIndex * pagesInRow + 1
    }

    /**
     * Get index of row for index of page
     * @param indexOfPage
     * @return
     */
    fun getRowForPageIndex(indexOfPage: Int): Int {
        return if (indexOfPage < pagesInRow) 0 else indexOfPage / pagesInRow // +1 - has header
    }

    companion object {
        private const val PAGES_IN_ROW_VERT = 2
        private const val PAGES_IN_ROW_HORIZ = 3
        private const val ROWS_COUNT_MIN_HORIZ = 2
        private const val ROWS_COUNT_MIN_VERT = 3 // Minimum shelfs on screen
        private var inflater: LayoutInflater? = null
    }

    init {
        this.activePageIndex = activePageIndex
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}