package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree;

import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo;
import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead;

import java.util.List;

/**
 * Returns common (ordinary folders) items of folders tree
 */
public class FoldersTreeCommonItemsGetter implements IFoldersTreeItemsGetter
{
    private final FolderInfo folderInfo;

    public FoldersTreeCommonItemsGetter(FolderInfo folderInfo)
    {
        this.folderInfo = folderInfo;
    }

    @Override
    public List<FoldersTreeItem> getSubItems(ICancelationTokenRead cancelationToken) {

        List<FoldersTreeItem> result= CollectionsHelper.transform(folderInfo.getSubFolders(),
                t -> new FoldersTreeItem(FoldersTreeItemTypes.Folder, t.getAbsolutePath(), cancelationToken));

        return result;
    }
}
