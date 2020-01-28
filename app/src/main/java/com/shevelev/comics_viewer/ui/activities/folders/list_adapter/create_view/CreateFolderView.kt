package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.IActivityFoldersActions
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.ui.activities.folders.list_adapter.IAdapterFoldersActions
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

class CreateFolderView(
    context: Activity,
    _inflater: LayoutInflater?,
    parentViewWidth: Int,
    private val activityActions: IActivityFoldersActions,
    private val adapterActions: IAdapterFoldersActions
) : CreateViewBase(context, _inflater, parentViewWidth) {

    override fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View {
        val disabled = foldersTreeItem == null || (!foldersTreeItem.isActive && !foldersTreeItem.hasImages)

        return if (!disabled) {
            val viewHolder = getView(R.layout.folders_list_item)
            viewHolder.text.text = diskItem.displayName
            if (!foldersTreeItem!!.hasImages) {
                viewHolder.checkBox.visibility = View.INVISIBLE
            }
            else {
                viewHolder.checkBox.setOnCheckedChangeListener {
                    checkBox: CompoundButton?, isChecked: Boolean -> adapterActions.folderCheckedChange(checkBox as CheckBox, diskItem)
                }
            }
            viewHolder.image.setImageResource(R.drawable.ic_folder)
            //            viewHolder.image.setOnClickListener(v -> activityActions.FolderTaped(diskItem.getId(), foldersTreeItem.getHasImages()));
//            viewHolder.text.setOnClickListener(v -> activityActions.FolderTaped(diskItem.getId(), foldersTreeItem.getHasImages()));
            viewHolder.view.setOnClickListener { v: View? -> activityActions.FolderTaped(diskItem.id, foldersTreeItem!!.hasImages) }
            viewHolder.view
        } else {
            val viewHolder = getView(R.layout.folders_list_item_disabled)
            viewHolder.text.text = diskItem.displayName
            viewHolder.image.setImageResource(R.drawable.ic_folder_disabled)
            viewHolder.image.setOnClickListener { v: View? -> }
            viewHolder.text.setOnClickListener { v: View? -> }
            viewHolder.view.setOnClickListener { v: View? -> }
            viewHolder.checkBox.visibility = View.INVISIBLE
            viewHolder.view
        }
    }
}