package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.dialogs.GotoPageDialog.InputModel
import com.shevelev.comics_viewer.common.dialogs.GotoPageDialog.OutputModel

/**
 * Dialog "Go to page"
 */
class GotoPageDialog(
    parentActivity: Activity?,
    okAction: (OutputModel) -> Unit,
    initModel: InputModel
) : CommonDialogBase<InputModel, OutputModel>(parentActivity!!, okAction, null, R.string.dialog_goto_page_title, R.layout.dialog_goto_page, initModel) {
    private var inputField: EditText? = null
    private var hint: TextView? = null

    /** Input model for dialog  */
    class InputModel(var currentPageNumber: Int, var maxPageNumber: Int)

    /** Output model for dialog  */
    class OutputModel(var pageNumber: Int)

    protected override fun initControls(view: View?, initModel: InputModel) {
        inputField = view!!.findViewById<View>(R.id.pageNumber) as EditText
        hint = view.findViewById<View>(R.id.hint) as TextView
        val pageNumberText = Integer.toString(initModel.currentPageNumber + 1)
        inputField!!.filters = arrayOf<InputFilter>(EditTextInputFilterMinMax(1, initModel.maxPageNumber))
        inputField!!.setText(pageNumberText)
        inputField!!.setSelection(pageNumberText.length) // Move cursor to end
        inputField!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                okButton!!.isEnabled = s.length > 0
            }
        })
        hint!!.text = String.format(getStringFromResources(R.string.goto_page_dialog_hint), 1, initModel.maxPageNumber)
    }

    override val outputModel: OutputModel
        protected get() {
            val value = inputField!!.text.toString()
            return OutputModel(value.toInt() - 1)
        }
}