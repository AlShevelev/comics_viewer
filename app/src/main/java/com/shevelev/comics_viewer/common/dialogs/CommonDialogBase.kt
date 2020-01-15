package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import com.shevelev.comics_viewer.App.Main.context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs

/**
 * Base class for common dialogs
 * @param <TI> input model
 * @param <TO> output model
</TO></TI> */
abstract class CommonDialogBase<TI, TO> protected constructor(
    private val parentActivity: Activity,
    private val okAction: IActionOneArgs<TO>,
    private val cancelAction: IActionZeroArgs?,
    titleResourceId: Int,
    dialogLayoutId: Int,
    initModel: TI) {
    protected var okButton: Button? = null
    protected var cancelButton: Button? = null
    private val title: String
    private val dialogLayoutId: Int
    private val initModel // Model to init dialog
        : TI

    protected fun getStringFromResources(resourceId: Int): String {
        return context!!.getString(resourceId)
    }

    private fun create(): AlertDialog {
        val inflater = parentActivity.layoutInflater
        var builder = AlertDialog.Builder(parentActivity)
        builder.setTitle(title)
        val view = inflater.inflate(dialogLayoutId, null)
        initControls(view, initModel)
        builder = builder.setView(view)
        setOkButton(builder, R.string.message_box_ok_button)
        setCancelButton(builder, R.string.message_box_cancel_button)
        return builder.create()
    }

    protected abstract fun initControls(view: View?, initModel: TI)

    protected abstract val outputModel: TO
    protected fun setOkButton(builder: AlertDialog.Builder, buttonTextResourceId: Int) {
        builder.setPositiveButton(buttonTextResourceId) { dialog: DialogInterface?, which: Int -> okAction.process(outputModel) }
    }

    protected fun setCancelButton(builder: AlertDialog.Builder, buttonTextResourceId: Int) {
        builder.setNegativeButton(buttonTextResourceId) { dialog: DialogInterface?, which: Int -> cancelAction?.process() }
    }

    fun show() {
        val dialog = create()
        dialog.show()
        okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE) // Buttons are accessible only after show()
        cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        afterButtonsInited()
    }

    protected open fun afterButtonsInited() {
        return
    }

    init {
        title = getStringFromResources(titleResourceId)
        this.dialogLayoutId = dialogLayoutId
        this.initModel = initModel
    }
}