package com.shevelev.comics_viewer.ui.activities.folders.file_system.disk_items

import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import com.shevelev.comics_viewer.utils.mapOrNull
import java.util.*

/**
 * Get list of disk sub-items for some folder
 */
class DiskItemsNormalProcessor(private val path: String) : IDiskItemsProcessor {

    companion object {
        private const val MAX_NAME_LEN = 35 // Max length of item
    }

    /**
     * @return List of disk items or empty list in case of error
     */
    override val diskItems: List<DiskItemInfo>
        get() = try {
            val folderInfo = FolderInfo(path)

            // Cut long names
            val folders = folderInfo.subFolders
                .mapOrNull { t: DiskItemInfo -> DiskItemInfo(t.id, t.itemType, cutName(t.name), t.name, t.absolutePath) }

            val files = folderInfo.files
                .mapOrNull { t: DiskItemInfo -> DiskItemInfo(t.id, t.itemType, cutName(t.name), t.name, t.absolutePath) }

            val images = folderInfo.images
                .mapOrNull { t: DiskItemInfo -> DiskItemInfo(t.id, t.itemType, cutName(t.name), t.name, t.absolutePath) }

            mergeLists(folders!!, files!!, images!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
            listOf()    // empty list
        }

    private fun cutName(name: String): String {
        return if (name.length <= MAX_NAME_LEN) name else name.substring(0, MAX_NAME_LEN - 4) + "..."
    }

    /**
     * Merge list in one for presentation
     */
    private fun mergeLists(foldersList: List<DiskItemInfo>, filesList: List<DiskItemInfo>, imagesList: List<DiskItemInfo>): List<DiskItemInfo> {
        Collections.sort(foldersList) { d1: DiskItemInfo, d2: DiskItemInfo -> d1.displayName.compareTo(d2.displayName) } // Sorting by name
        Collections.sort(filesList) { d1: DiskItemInfo, d2: DiskItemInfo -> d1.displayName.compareTo(d2.displayName) }
        Collections.sort(imagesList) { d1: DiskItemInfo, d2: DiskItemInfo -> d1.displayName.compareTo(d2.displayName) }

        val result: MutableList<DiskItemInfo> = mutableListOf()

        result.addAll(foldersList)
        result.addAll(imagesList) // And merging
        result.addAll(filesList)
        return result
    }
}