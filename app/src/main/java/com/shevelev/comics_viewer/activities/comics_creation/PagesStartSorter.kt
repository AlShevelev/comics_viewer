package com.shevelev.comics_viewer.activities.comics_creation

import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import java.util.*

/**
 * Start sort pagers of comics
 */
object PagesStartSorter {
    /**
     * Comparator for sorting images
     */
    private class ImagesComparator : Comparator<DiskItemInfo> {
        override fun compare(diskItem1: DiskItemInfo, diskItem2: DiskItemInfo): Int =
            diskItem1.displayName.compareTo(diskItem2.displayName, ignoreCase = true)
    }

    fun sort(pathToFolder: String): List<DiskItemInfo> {
        val folderInfo = FolderInfo(pathToFolder)
        val images = folderInfo.images

        if (CollectionsHelper.isNullOrEmpty(images)) { //Empty list if no images
            return ArrayList()
        }
        val comparator: Comparator<DiskItemInfo> = ImagesComparator()
        Collections.sort(images, comparator)
        return images
    }
}