package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.graphics.drawable.ColorDrawable
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.widget.ListView

/**
 * Listener for dragging events for item in list
 */
class ListItemOnDragListener internal constructor(
    private val _item: ListItemDrag,
    private val _dragColor: Int,
    private val onDrag: (ListItemDragingInfo) -> Unit
) : OnDragListener {
    private var _normalColor = 0

    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {
                _normalColor = (v.background as ColorDrawable).color
                v.setBackgroundColor(_dragColor)
            }

            DragEvent.ACTION_DRAG_EXITED -> v.setBackgroundColor(_normalColor)

            DragEvent.ACTION_DROP -> {
                val passObj = event.localState as PassObjectDrag
                val view = passObj.view
                val passedItem = passObj.item
                val srcList = passObj.srcList
                val oldParent = view.parent as ListView
                val srcAdapter = oldParent.adapter as ListDragAdapter
                val newParent = v.parent as ListView
                val destAdapter = newParent.adapter as ListDragAdapter
                val destList = destAdapter.list
                val removeLocation = srcList.indexOf(passedItem)
                val insertLocation = destList.indexOf(_item)
                if (removeLocation != insertLocation) onDrag(ListItemDragingInfo(passedItem, srcList, destList, srcAdapter, destAdapter, insertLocation))
                v.setBackgroundColor(_normalColor)
            }

            DragEvent.ACTION_DRAG_ENDED -> v.setBackgroundColor(_normalColor)
        }
        return true
    }
}