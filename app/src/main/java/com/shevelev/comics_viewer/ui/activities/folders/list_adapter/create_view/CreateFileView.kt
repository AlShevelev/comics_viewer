package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

class CreateFileView(
    context: Activity,
    inflater: LayoutInflater?,
    parentViewWidth: Int
) : CreateViewBase(context, inflater, parentViewWidth) {

    override fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View {
        val viewHolder = getView(R.layout.folders_list_item)
        viewHolder.text.text = diskItem.displayName
        viewHolder.image.setImageResource(R.drawable.ic_file)
        viewHolder.checkBox.visibility = View.INVISIBLE
        viewHolder.image.setOnClickListener { v: View? -> } // For prevent clicking track on checked item
        viewHolder.text.setOnClickListener { v: View? -> }
        return viewHolder.view
    }
}