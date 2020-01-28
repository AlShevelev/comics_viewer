package com.shevelev.comics_viewer.ui.activities.comics_creation

import android.app.Activity
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.dialogs.ComicsNameDialog
import com.shevelev.comics_viewer.common.dialogs.MessageBoxHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo

/**
 * First step of comics creation - checking creation posibility and choosing name
 */
class ChooseComicsName(
    private val context: Activity,
    private val nameChoose:( (String, Boolean, String) -> Unit)?
) {
    private lateinit var pathToFolder: String
    /**
     * It's magic - we createFromFile comics here
     */
    fun startCreate(pathToFolder: String) {
        this.pathToFolder = pathToFolder

        val folderInfo = FolderInfo(this.pathToFolder)
        val canCreate = folderInfo.images.isNotEmpty()
        if (canCreate) {
            startGetComicsName(folderInfo.name)
        }
        else {
            MessageBoxHelper.createOkDialog(
                context,
                context.getString(R.string.message_box_cant_create_comics_title),
                context.getString(R.string.message_box_no_imges_text)
            ) {  }.show()
        }
    }

    private fun startGetComicsName(initName: String) {
        val dialog = ComicsNameDialog(
            context,                        // We get name here
            { result: ComicsNameDialog.Model -> nameChoose?.invoke(result.title, result.isPrivate, pathToFolder) },
            { }, R.string.dialog_comics_name_title, R.layout.dialog_enter_name, ComicsNameDialog.Model(initName, false))
        dialog.show()
    }
}