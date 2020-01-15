package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import com.shevelev.comics_viewer.R

/**
 * Dialog for setDiskItems/edit comics password
 */
class CreatePasswordDialog : CommonDialogBase<CreatePasswordDialog.Model, CreatePasswordDialog.Model> {
    private var passwordControl: EditText? = null
    private var hintControl: EditText? = null

    /**
     * Model for dialog
     */
    class Model(var password: String, var hint: String) {

    }

    constructor(
        parentActivity: Activity?,
        okAction: (Model) -> Unit,
        cancelAction: (() -> Unit)?
    ) : super(parentActivity!!, okAction, cancelAction, R.string.dialog_create_password_title, R.layout.dialog_create_password, Model("", "")) {}
    constructor(
        parentActivity: Activity?,
        okAction: (Model) -> Unit,
        cancelAction: (() -> Unit)?,
        titleId: Int,
        layoutId: Int,
        password: String) : super(parentActivity!!, okAction, cancelAction, titleId, layoutId, Model(password, "")) {
    }

    protected override fun initControls(view: View?, initModel: Model) {
        passwordControl = view!!.findViewById<View>(R.id.password) as EditText
        passwordControl!!.setText(initModel.password)
        passwordControl!!.setSelection(initModel.password.length) // Move cursor to end
        hintControl = view.findViewById<View>(R.id.passwordHint) as EditText
        hintControl!!.setText(initModel.hint)
        hintControl!!.setSelection(initModel.hint.length)
        val showPasswordCheckbox = view.findViewById<View>(R.id.showPasswordCheckbox) as CheckBox
        passwordControl!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                okButton!!.isEnabled = s.length > 0 // Disable ok button
            }
        })
        showPasswordCheckbox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) passwordControl!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL else passwordControl!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordControl!!.setSelection(passwordControl!!.text.toString().length) // Move cursor to end
        }
    }

    override val outputModel: Model
        protected get() = Model(passwordControl!!.text.toString(), hintControl!!.text.toString())

    override fun afterButtonsInited() {
        okButton!!.isEnabled = passwordControl!!.text.length > 0 // Disable ok button
    }
}