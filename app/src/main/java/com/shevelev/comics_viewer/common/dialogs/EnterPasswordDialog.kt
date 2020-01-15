package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs

/**
 * Dialog for enter comics password
 */
class EnterPasswordDialog(
    parentActivity: Activity?,
    okAction: IActionOneArgs<String>,
    cancelAction: IActionZeroArgs?,
    hint: String) : CommonDialogBase<EnterPasswordDialog.Model, String>(
    parentActivity!!,
    okAction,
    cancelAction,
    R.string.dialog_enter_password_title,
    R.layout.dialog_enter_password,
    Model("", hint)
) {
    private var passwordControl: EditText? = null

    /**
     * Model for dialog
     */
    class Model(var password: String, var hint: String)

    protected override fun initControls(view: View?, initModel: Model) {
        passwordControl = view!!.findViewById<View>(R.id.password) as EditText
        passwordControl!!.setText(initModel.password)
        passwordControl!!.setSelection(initModel.password.length) // Move cursor to end
        val hintControl = view.findViewById<View>(R.id.passwordHint) as TextView
        hintControl.text = getStringFromResources(R.string.dialog_passwordHint_enter) + " " + initModel.hint
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

    override val outputModel: String
        protected get() = passwordControl!!.text.toString()

    override fun afterButtonsInited() {
        okButton!!.isEnabled = false
    }
}