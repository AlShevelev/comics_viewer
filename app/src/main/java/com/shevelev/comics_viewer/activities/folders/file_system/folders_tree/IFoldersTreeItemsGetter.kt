package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import com.shevelev.comics_viewer.core.cancelation.ICancelationTokenRead

/**
 * Interface of class for getting tree items
 */
interface IFoldersTreeItemsGetter {
    fun getSubItems(cancellationToken: com.shevelev.comics_viewer.core.cancelation.ICancelationTokenRead): List<FoldersTreeItem>?
}