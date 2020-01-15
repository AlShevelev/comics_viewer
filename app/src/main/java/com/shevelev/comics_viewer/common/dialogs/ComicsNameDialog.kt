package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs

/**
 * Dialog for name of comics
 */
class ComicsNameDialog(
    parentActivity: Activity?,
    okAction: IActionOneArgs<Model>,
    cancelAction: IActionZeroArgs?,
    titleResourceId: Int,
    dialogLayoutId: Int,
    initModel: Model
) : CommonDialogBase<ComicsNameDialog.Model, ComicsNameDialog.Model>(
    parentActivity!!,
    okAction,
    cancelAction,
    titleResourceId,
    dialogLayoutId,
    initModel) {

    private var textControl: EditText? = null
    private var isPrivateComicsControl: CheckBox? = null

    /**
     * Model for dialog
     */
    class Model(var title: String, var isPrivate: Boolean)

    protected override fun initControls(view: View?, initModel: Model) {
        textControl = view!!.findViewById<View>(R.id.et_comics_name) as EditText
        textControl!!.setText(initModel.title)
        textControl!!.setSelection(initModel.title.length) // Move cursor to end
        isPrivateComicsControl = view.findViewById<View>(R.id.isPrivateComics) as CheckBox
        isPrivateComicsControl!!.isChecked = initModel.isPrivate
        textControl!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                okButton!!.isEnabled = s.length > 0
            }
        })
    }

    override val outputModel: Model
        protected get() = Model(textControl!!.text.toString(), isPrivateComicsControl!!.isChecked)
}