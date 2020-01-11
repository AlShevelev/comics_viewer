package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead

/**
 * Returns common (ordinary folders) items of folders tree
 */
class FoldersTreeCommonItemsGetter(private val folderInfo: FolderInfo) : IFoldersTreeItemsGetter {
    override fun getSubItems(cancellationToken: ICancelationTokenRead): List<FoldersTreeItem>? =
        CollectionsHelper.transform(folderInfo.subFolders) {
            t: DiskItemInfo -> FoldersTreeItem(FoldersTreeItemType.FOLDER, t.absolutePath, cancellationToken)
        }
}