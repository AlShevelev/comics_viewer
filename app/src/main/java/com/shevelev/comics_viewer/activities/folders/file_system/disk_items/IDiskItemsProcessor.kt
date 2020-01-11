package com.shevelev.comics_viewer.activities.folders.file_system.disk_items

import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo

interface IDiskItemsProcessor {
    val diskItems: List<DiskItemInfo>
}