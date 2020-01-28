package com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations

import android.content.Context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.ui.activities.view_comics.CurlActivity
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.dal.DalFacade
import java.util.*

/**
 * Operation for view comics
 */
class ViewComicsOperation(
    private val activity: IOneComicsActivity?,
    private val keyValueStorage: KeyValueStorageFacade
) : ComicsOperationBase(activity!!) {
    private enum class ComicsOpenConditions {
        CanOpen, ShowCreatePasswordDialog, ShowEnterPasswordDialog
    }

    /**   */
    fun start(comicsId: Long) {
        val condition = getOpenComicsConditions(comicsId)
        when (condition) {
            ComicsOpenConditions.CanOpen -> startView(comicsId)
            ComicsOpenConditions.ShowCreatePasswordDialog -> createPasswordAndView(comicsId)
            ComicsOpenConditions.ShowEnterPasswordDialog -> enterPasswordAndView(comicsId)
        }
    }

    /**   */
    fun complete(comicsId: Long) {
        uiMethods.updateBooksList(comicsId)
    }

    /** Can we open comics?  */
    private fun getOpenComicsConditions(comicsId: Long): ComicsOpenConditions {
        val comics = DalFacade.Comics.getComicsById(comicsId)
        if (!comics!!.isPrivate) return ComicsOpenConditions.CanOpen // Public comics can be opened without conditions

        return if (keyValueStorage.getPassword() != null) {
            if (keyValueStorage.getPasswordEntered() != null) ComicsOpenConditions.CanOpen else ComicsOpenConditions.ShowEnterPasswordDialog
        } else ComicsOpenConditions.ShowCreatePasswordDialog // No password - need create
    }

    /**   */
    private fun startView(comicsId: Long) {
        if (uiMethods.isUserActionsLock) return
        DalFacade.Comics.updateLastViewDate(comicsId, Date()) // UTC
        CurlActivity.start(context, comicsId) // Start view comics
    }

    /**   */
    private fun createPasswordAndView(comicsId: Long) {
        ToastsHelper.Show(activity as Context, R.string.private_comics_create_password_message, ToastsHelper.Position.Bottom)
        val dialog = CreatePasswordDialog(
            context,
            { result: CreatePasswordDialog.Model ->
                keyValueStorage.savePassword(result.password)
                keyValueStorage.savePasswordHint(result.hint)
                keyValueStorage.savePasswordEntered(true)
                startView(comicsId)
            },
            { })
        dialog.show()
    }

    /**   */
    private fun enterPasswordAndView(comicsId: Long) {
        val password = keyValueStorage.getPassword()
        val hint = keyValueStorage.getPasswordHint()
        val dialog = EnterPasswordDialog(
            context,
            { result: String ->
                if (result == password) {
                    keyValueStorage.savePasswordEntered(true)
                    startView(comicsId)
                } else ToastsHelper.Show(activity as Context, R.string.message_invalid_password, ToastsHelper.Position.Center)
            },
            { }, hint!!)
        dialog.show()
    }
}