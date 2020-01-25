package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import com.shevelev.comics_viewer.utils.mapOrNull
import com.shevelev.comics_viewer.core.cancelation.ICancelationTokenRead

/**
 * Returns common (ordinary folders) items of folders tree
 */
class FoldersTreeCommonItemsGetter(private val folderInfo: FolderInfo) : IFoldersTreeItemsGetter {
    override fun getSubItems(cancellationToken: com.shevelev.comics_viewer.core.cancelation.ICancelationTokenRead): List<FoldersTreeItem>? =
        folderInfo.subFolders.mapOrNull { FoldersTreeItem(FoldersTreeItemType.FOLDER, it.absolutePath, cancellationToken) }
}