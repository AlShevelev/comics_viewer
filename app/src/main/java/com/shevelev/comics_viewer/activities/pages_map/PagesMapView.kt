package com.shevelev.comics_viewer.activities.pages_map

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.ListViewHelper
import com.shevelev.comics_viewer.common.structs.Size
import com.shevelev.comics_viewer.dal.dto.Comics
import com.shevelev.comics_viewer.dal.dto.Page

class PagesMapView(
    context: Context?,
    comics: Comics,
    pages: List<Page>,
    private val onChangeActivePage: (Int) -> Unit,
    private val onZoomPage: (Int) -> Unit) : RelativeLayout(context) {
    private var parentSize: Size? = null
    private var pagesMapAdapter: PagesMapAdapter? = null
    private var activePageIndex: Int
    private val pages: List<Page>
    private val pagesList: ListView
    val size: Size
        get() = Size(width, height)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentSize = Size(w, h)
        pagesMapAdapter = PagesMapAdapter((context as Activity), pages, activePageIndex, onChangeActivePage, onZoomPage)
        pagesMapAdapter!!.setParentSize(parentSize!!)
        pagesList.adapter = pagesMapAdapter
        scrollToPage(activePageIndex)
    }

    fun scrollToPage(pageIndex: Int) {
        if (pagesList.canScrollVertically(1)) {
            val indexOfRow = pagesMapAdapter!!.getRowForPageIndex(pageIndex)
            pagesList.setSelection(indexOfRow)
        }
    }

    /**
     * Update controls after active page changed
     */
    fun updatePageControls(newActivePageIndex: Int) {
        pagesMapAdapter!!.setActivePageIndex(newActivePageIndex)
        val oldActivePageRow = pagesMapAdapter!!.getRowForPageIndex(activePageIndex)
        val newActivePageRow = pagesMapAdapter!!.getRowForPageIndex(newActivePageIndex)
        ListViewHelper.invalidateListItem(oldActivePageRow, pagesList)
        if (oldActivePageRow != newActivePageRow) ListViewHelper.invalidateListItem(newActivePageRow, pagesList)
        activePageIndex = newActivePageIndex
    }

    init {
        activePageIndex = comics.lastViewedPageIndex
        this.pages = pages
        View.inflate(context, R.layout.activity_pages_map, this)
        pagesList = findViewById<View>(R.id.pagesList) as ListView
        pagesList.divider = null
        pagesList.dividerHeight = 0
    }
}