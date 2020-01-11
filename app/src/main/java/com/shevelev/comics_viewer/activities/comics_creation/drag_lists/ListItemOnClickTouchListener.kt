package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import com.shevelev.comics_viewer.activities.comics_creation.ISortPagesActivityItemsEvents
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.structs.Area
import com.shevelev.comics_viewer.common.structs.Point
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Show hint after click list item
 */
class ListItemOnClickTouchListener(private val events: ISortPagesActivityItemsEvents) : OnItemClickListener, OnTouchListener {
    private var touchPos : Point? = null        // Touch position in listView

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (touchPos == null) {
            return
        }

        var area = getAreaByTag(view, "zoomIcon", 0, 0)
        if (area!!.isHit(touchPos)) events.onZoomItem(position) else {
            area = getAreaByTag(view, "visibilityIcon", 0, 0)
            if (area!!.isHit(touchPos)) events.onSetVisibilityItem(position) else ToastsHelper.Show( // Show tag with item's name
                (parent.getItemAtPosition(position) as ListItemDrag).itemLongString,
                ToastsHelper.Position.Bottom)
        }
        touchPos = null
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            touchPos = Point(event.x.toInt(), event.y.toInt())
        }
        return false
    }

    /**
     * Get control area with some tag
     */
    private fun getAreaByTag(view: View, lookedTag: String, top: Int, left: Int): Area? {
        val topInner = top + view.left
        val leftInner = left + view.left

        val tag = view.tag
        if (tag != null && tag == lookedTag) return Area(Point(leftInner, topInner), Size(view.width, view.height))
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val area = getAreaByTag(view.getChildAt(i), lookedTag, topInner, leftInner)
                if (area != null) return area
            }
        }
        return null
    }
}