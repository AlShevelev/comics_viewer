package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.content.ClipData
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener

/**
 * Listener for long click on item (to start drag)
 */
class ListItemLongClickListener : OnItemLongClickListener {
    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        val selectedItem = parent.getItemAtPosition(position) as ListItemDrag
        val associatedAdapter = parent.adapter as ListDragAdapter
        val associatedList = associatedAdapter.list
        val passObj = PassObjectDrag(view, selectedItem, associatedList)
        val data = ClipData.newPlainText("", "") // Start drag on long click
        val shadowBuilder = DragShadowBuilder(view)
        view.startDragAndDrop(data, shadowBuilder, passObj, 0)
        return true
    }
}