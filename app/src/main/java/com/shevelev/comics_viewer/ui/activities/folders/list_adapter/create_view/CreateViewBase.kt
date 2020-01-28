package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

abstract class CreateViewBase(
    private val context: Activity,
    private val _inflater: LayoutInflater?,
    private val parentViewWidth: Int
) : ICreateView {
    /**
     * Temporary data struct vith view
     */
    protected inner class ViewHolder(var view: View, var text: TextView, var image: ImageView, var checkBox: CheckBox)

    protected fun getView(viewResId: Int): ViewHolder {
        val rowView = _inflater!!.inflate(viewResId, null)
        rowView.minimumWidth = parentViewWidth
        val tv = rowView.findViewById<View>(R.id.tvDiskItemName) as TextView
        val img = rowView.findViewById<View>(R.id.ivDiskItemIcon) as ImageView
        val cb = rowView.findViewById<View>(R.id.cbDiskItemCheck) as CheckBox
        return ViewHolder(rowView, tv, img, cb)
    }

    abstract override fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View
}