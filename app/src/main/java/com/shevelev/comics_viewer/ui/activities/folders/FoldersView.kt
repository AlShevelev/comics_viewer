package com.shevelev.comics_viewer.ui.activities.folders

import android.app.Activity
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTree
import com.shevelev.comics_viewer.ui.activities.folders.list_adapter.FoldersListAdapter
import com.shevelev.comics_viewer.common.custom_controls.ProgressBar
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

/**
 * Main view for folders list
 */
class FoldersView(
    private val context: Activity,
    layoutId: Int,
    private val actions: IActivityFoldersActions,
    private val foldersTree: FoldersTree
) : RelativeLayout(context) {
    private val foldersList : ListView
    private val previewProgressBar: ProgressBar
    private var listAdapter: FoldersListAdapter? = null

    fun updateDiskItems(diskItems: List<DiskItemInfo>) {
        listAdapter = FoldersListAdapter(context, diskItems, actions, foldersTree)
        foldersList.adapter = listAdapter
    }

    fun showProgress() {
        previewProgressBar.show()
    }

    fun hideProgress() {
        previewProgressBar.hide()
    }

    init {
        View.inflate(context, layoutId, this)
        foldersList = findViewById<View>(R.id.lvFolders) as ListView
        previewProgressBar = findViewById<View>(R.id.previewProgressBar) as ProgressBar
    }
}