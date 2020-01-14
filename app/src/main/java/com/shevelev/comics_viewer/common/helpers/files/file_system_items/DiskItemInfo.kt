package com.shevelev.comics_viewer.common.helpers.files.file_system_items

/**
 * One significant item of file system - device, memory card, folder, image or non-image file
 */
class DiskItemInfo(val id: Int, val itemType: DiskItemTypes,
                   /**
                    * Display name - may be cutted and formatted
                    */
                   val displayName: String,
                   /**
                    * Name of file - uncutted and unformatted
                    */
                   val name: String,
                   /**
                    * full path to item (without name)
                    */
                   val absolutePath: String) {

    /**
     * full name of file (with path)
     */
    val fullname: String
        get() = absolutePath + "/" + name

}