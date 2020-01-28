package com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree

import android.os.Environment
import java.io.File

/**
 * Returns root items of folders tree
 */
class FoldersTreeRootItemsGetter : IFoldersTreeItemsGetter {
    override fun getSubItems(cancellationToken: com.shevelev.comics_viewer.core.cancelation.ICancelationTokenRead): List<FoldersTreeItem>? {
        val result: MutableList<FoldersTreeItem> = mutableListOf()
        val storageState = Environment.getExternalStorageState()

        if (storageState != Environment.MEDIA_MOUNTED) // No mounted disks
            return result

        val dataFolder = Environment.getExternalStorageDirectory()
        if (dataFolder != null) // Main storage
            result.add(FoldersTreeItem(FoldersTreeItemType.DEVICE, dataFolder.absolutePath, cancellationToken))

        val sdCardFolderPath = System.getenv("SECONDARY_STORAGE")
        if (sdCardFolderPath != null) { // Card's storage
            val sdCardFolder = File(System.getenv("SECONDARY_STORAGE"))
            result.add(FoldersTreeItem(FoldersTreeItemType.SD_CARD, sdCardFolder.absolutePath, cancellationToken))
        }
        return result
    }
}