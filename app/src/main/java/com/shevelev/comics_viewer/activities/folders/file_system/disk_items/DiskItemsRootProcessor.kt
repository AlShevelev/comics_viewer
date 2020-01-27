package com.shevelev.comics_viewer.activities.folders.file_system.disk_items

import android.content.Context
import android.os.Environment
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemTypes
import java.io.File

/**
 * Get list of root disk sub-items
 */
class DiskItemsRootProcessor(private val context: Context) : IDiskItemsProcessor {
    override val diskItems: List<DiskItemInfo>
        get() {
            val result: MutableList<DiskItemInfo> = mutableListOf()

            val storageState = Environment.getExternalStorageState()
            if (storageState != Environment.MEDIA_MOUNTED) {
                return result
            }
            val dataFolder = Environment.getExternalStorageDirectory()
            if (dataFolder != null) result.add(DiskItemInfo(
                0,
                DiskItemTypes.Device,
                context.getString(R.string.dev_root_folder),
                context.getString(R.string.dev_root_folder),
                dataFolder.absolutePath))
            val sdCardFolderPath = System.getenv("SECONDARY_STORAGE")

            if (sdCardFolderPath != null) {
                val sdCardFolder = File(System.getenv("SECONDARY_STORAGE"))
                if (sdCardFolder != null)
                    result.add(DiskItemInfo(
                    1,
                    DiskItemTypes.SdCard,
                    context.getString(R.string.sdcard_root_folder),
                    context.getString(R.string.sdcard_root_folder),
                    sdCardFolder.absolutePath))
            }
            return result
        }
}