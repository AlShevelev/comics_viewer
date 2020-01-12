package com.shevelev.comics_viewer.activities.main.one_comics_working.operations

import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.activities.view_comics.CurlActivity
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Option
import com.shevelev.comics_viewer.options.OptionsFacade
import com.shevelev.comics_viewer.options.OptionsKeys
import com.shevelev.comics_viewer.options.OptionsValues
import java.util.*

/**
 * Operation for view comics
 */
class ViewComicsOperation(activity: IOneComicsActivity?) : ComicsOperationBase(activity!!) {
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
        if (!comics.isPrivate) return ComicsOpenConditions.CanOpen // Public comics can be opened without conditions
        return if (OptionsFacade.LongLivings[OptionsKeys.Password] != null) {
            if (OptionsFacade.ShortLivings[OptionsKeys.PasswordEntered] != null) ComicsOpenConditions.CanOpen else ComicsOpenConditions.ShowEnterPasswordDialog
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
        ToastsHelper.Show(R.string.private_comics_create_password_message, ToastsHelper.Position.Bottom)
        val dialog = CreatePasswordDialog(
            context,
            IActionOneArgs { result: CreatePasswordDialog.Model ->
                OptionsFacade.LongLivings.addOrUpdate(arrayOf(Option(OptionsKeys.Password, result.password), Option(OptionsKeys.PasswordsHint, result.hint)))
                OptionsFacade.ShortLivings.addOrUpdate(arrayOf(Option(OptionsKeys.PasswordEntered, OptionsValues.True)))
                startView(comicsId)
            },
            IActionZeroArgs {})
        dialog.show()
    }

    /**   */
    private fun enterPasswordAndView(comicsId: Long) {
        val password = OptionsFacade.LongLivings[OptionsKeys.Password]
        val hint = OptionsFacade.LongLivings[OptionsKeys.PasswordsHint]
        val dialog = EnterPasswordDialog(
            context,
            IActionOneArgs { result: String ->
                if (result == password) {
                    OptionsFacade.ShortLivings.addOrUpdate(arrayOf(Option(OptionsKeys.PasswordEntered, OptionsValues.True)))
                    startView(comicsId)
                } else ToastsHelper.Show(R.string.message_invalid_password, ToastsHelper.Position.Center)
            },
            IActionZeroArgs {}, hint)
        dialog.show()
    }
}