package com.shevelev.comics_viewer.common

import android.widget.ListView

object ListViewHelper {
    /**
     * Invalidate item on listView with some index
     * @param itemIndex
     * @param list
     */
    fun invalidateListItem(itemIndex: Int, list: ListView) {
        val start = list.firstVisiblePosition
        var i = start
        val j = list.lastVisiblePosition
        while (i <= j) {
            if (i == itemIndex) {
                val view = list.getChildAt(i - start)
                list.adapter.getView(i, view, list) // Re-setDiskItems control
                break
            }
            i++
        }
    }
}