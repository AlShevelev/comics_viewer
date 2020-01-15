package com.shevelev.comics_viewer.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs

/**
 * Create message boxes
 */
object MessageBoxHelper {
    /**
     * Simple message box with Ok button
     */
    fun createOkDialog(context: Context, title: String?, message: String?, okAction: IActionZeroArgs): Dialog {
        val dlgAlert = AlertDialog.Builder(context)
        dlgAlert.setTitle(title)
        dlgAlert.setMessage(message)
        dlgAlert.setPositiveButton(context.getString(R.string.message_box_ok_button)) { dialog: DialogInterface?, which: Int -> okAction.process() }
        dlgAlert.setCancelable(false)
        return dlgAlert.create()
    }

    /**
     * Simple message box with Yes and No buttons
     */
    fun createYesNoDialog(context: Context, title: String?, message: String?, yesAction: IActionZeroArgs?, noAction: IActionZeroArgs?): Dialog {
        val dlgAlert = AlertDialog.Builder(context)
        dlgAlert.setTitle(title)
        dlgAlert.setMessage(message)
        dlgAlert.setPositiveButton(context.getString(R.string.message_box_yes_button)) { dialog: DialogInterface?, which: Int -> yesAction?.process() }
        dlgAlert.setNegativeButton(context.getString(R.string.message_box_no_button)) { dialog: DialogInterface?, which: Int -> noAction?.process() }
        dlgAlert.setCancelable(true)
        return dlgAlert.create()
    }
}