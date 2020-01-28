package com.shevelev.comics_viewer.ui.activities.folders

interface IActivityFoldersActions {
    /**
     * Folder's ckeckbox was changed by User
     * @param id Id of folder
     */
    fun FolderCheckChanged(id: Int, isChecked: Boolean)

    /**
     * User taped on folder
     * @param id Id of folder
     * @param folderWithImages true if this folder containts images
     */
    fun FolderTaped(id: Int, folderWithImages: Boolean)
}