package com.shevelev.comics_viewer.activities.folders.file_system.disk_items;

import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;

import java.util.List;

/**
 * Created by Syleiman on 26.07.2015.
 */
public interface IDiskItemsProcessor
{
    List<DiskItemInfo> getDiskItems();
}
