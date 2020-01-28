package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.IActivityFoldersActions
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

class CreateDeviceView(
    context: Activity,
    _inflater: LayoutInflater?,
    parentViewWidth: Int,
    private val actions: IActivityFoldersActions
) : CreateViewBase(context, _inflater, parentViewWidth) {

    override fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View {
        val disabled = foldersTreeItem == null || !foldersTreeItem.isActive
        return if (!disabled) {
            val viewHolder = getView(R.layout.folders_list_item)
            viewHolder.text.text = diskItem.displayName
            viewHolder.checkBox.visibility = View.INVISIBLE
            viewHolder.image.setImageResource(R.drawable.ic_cell_phone2)
            //            viewHolder.image.setOnClickListener(v -> actions.FolderTaped(diskItem.getId(), false));
//            viewHolder.text.setOnClickListener(v -> actions.FolderTaped(diskItem.getId(), false));
            viewHolder.view.setOnClickListener { v: View? -> actions.FolderTaped(diskItem.id, false) }
            viewHolder.view
        } else {
            val viewHolder = getView(R.layout.folders_list_item_disabled)
            viewHolder.text.text = diskItem.displayName
            viewHolder.checkBox.visibility = View.INVISIBLE
            viewHolder.image.setImageResource(R.drawable.ic_cell_phone2_disabled)
            viewHolder.image.setOnClickListener { v: View? -> }
            viewHolder.text.setOnClickListener { v: View? -> }
            viewHolder.view.setOnClickListener { v: View? -> }
            viewHolder.view
        }
    }
}