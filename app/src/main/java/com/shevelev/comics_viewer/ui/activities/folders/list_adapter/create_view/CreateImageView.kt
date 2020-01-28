package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.common.dialogs.ZoomedPagePreviewDialog
import com.shevelev.comics_viewer.common.helpers.ScreenHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

class CreateImageView(
    private val context: Activity,
    _inflater: LayoutInflater?,
    parentViewWidth: Int
) : CreateViewBase(context, _inflater, parentViewWidth) {

    override fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View {
        val viewHolder = getView(R.layout.folders_list_item)
        viewHolder.text.text = diskItem.displayName
        viewHolder.image.setImageResource(R.drawable.ic_image)
        viewHolder.checkBox.visibility = View.INVISIBLE
        viewHolder.view.setOnClickListener { v: View? -> showImage(diskItem) }
        return viewHolder.view
    }

    private fun showImage(diskItem: DiskItemInfo) {
        val dialog = ZoomedPagePreviewDialog(
            context,
            diskItem.fullname,
            ScreenHelper.getScreenSize(context).scale(0.85f))
        dialog.show()
    }
}