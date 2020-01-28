package com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view

import android.view.View
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

interface ICreateView {
    fun CreateView(diskItem: DiskItemInfo, foldersTreeItem: FoldersTreeItem?): View?
}