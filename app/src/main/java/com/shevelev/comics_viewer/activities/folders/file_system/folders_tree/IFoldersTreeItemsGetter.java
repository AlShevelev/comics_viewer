package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree;

import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead;

import java.util.List;

/**
 * Interface of class for getting tree items
 */
public interface IFoldersTreeItemsGetter
{
    List<FoldersTreeItem> getSubItems(ICancelationTokenRead cancelationToken);
}
