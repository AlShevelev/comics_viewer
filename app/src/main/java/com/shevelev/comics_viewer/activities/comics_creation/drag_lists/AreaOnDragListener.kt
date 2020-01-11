package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.widget.ListView
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs

/**
 * Listener for dragging on area (not into other list)
 */
class AreaOnDragListener(private val _onDrag: IActionOneArgs<ListItemDragingInfo>?) : OnDragListener {
    override fun onDrag(v: View, event: DragEvent): Boolean {
        if (event.action == DragEvent.ACTION_DROP) {
            val passObj = event.localState as PassObjectDrag
            val view = passObj.view
            val passedItem = passObj.item
            val srcList = passObj.srcList
            val oldParent = view.parent as ListView
            val srcAdapter = oldParent.adapter as ListDragAdapter
            val newParent = v as LinearLayoutDrag
            val destAdapter = newParent.listView!!.adapter as ListDragAdapter
            val destList = destAdapter.list
            _onDrag?.process(ListItemDragingInfo(passedItem, srcList, destList, srcAdapter, destAdapter, 0))
            //smooth scroll to bottom
            newParent.listView!!.smoothScrollToPosition(destAdapter.count - 1)
        }
        return true
    }
}