package com.shevelev.comics_viewer.activities.folders.list_adapter;

import android.widget.CheckBox;

import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;

public interface IAdapterFoldersActions
{
    void folderCheckedChange(CheckBox checkBox, DiskItemInfo diskItem);
}
