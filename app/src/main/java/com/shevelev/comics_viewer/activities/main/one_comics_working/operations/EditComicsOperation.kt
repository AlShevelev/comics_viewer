package com.shevelev.comics_viewer.activities.main.one_comics_working.operations

import android.content.Context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.common.dialogs.ComicsNameDialog
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.dal.DalFacade

/**
 * Operation for edit comics
 */
class EditComicsOperation(private val activity: IOneComicsActivity?) : ComicsOperationBase(activity!!) {
    fun start(comicsId: Long) {
        val comics = DalFacade.Comics.getComicsById(comicsId)
        val dialog = ComicsNameDialog(
            context,
            { result: ComicsNameDialog.Model ->
                // We get name here
                val updateResult = DalFacade.Comics.updateNameAndHidden(comicsId, result.title, result.isPrivate)
                if (updateResult) {
                    uiMethods.updateBooksList(null)
                } else {
                    ToastsHelper.Show(activity as Context, R.string.message_cant_update_comics_title, ToastsHelper.Position.Center)
                }
            },
            { }, R.string.dialog_comics_name_title, R.layout.dialog_enter_name, ComicsNameDialog.Model(comics!!.name, comics.isPrivate))
        dialog.show()
    }
}