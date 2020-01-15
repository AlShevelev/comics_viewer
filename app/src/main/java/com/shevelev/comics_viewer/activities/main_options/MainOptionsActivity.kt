package com.shevelev.comics_viewer.activities.main_options

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.ActivityCodes
import com.shevelev.comics_viewer.activities.ActivityResultCodes
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.dal.dto.Option
import com.shevelev.comics_viewer.options.OptionsFacade
import com.shevelev.comics_viewer.options.OptionsKeys
import com.shevelev.comics_viewer.options.OptionsValues

class MainOptionsActivity : Activity() {
    private var createPasswordControl: LinearLayout? = null
    private var enterPasswordControl: LinearLayout? = null
    private var changePasswordControl: LinearLayout? = null
    private var passwordEnteredOnStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_options)
        createPasswordControl = findViewById<View>(R.id.createPassword) as LinearLayout
        enterPasswordControl = findViewById<View>(R.id.enterPassword) as LinearLayout
        changePasswordControl = findViewById<View>(R.id.changePassword) as LinearLayout
        createPasswordControl!!.setOnClickListener { v: View? -> onCreatePasswordClick() }
        enterPasswordControl!!.setOnClickListener { v: View? -> onEnterPasswordClick() }
        changePasswordControl!!.setOnClickListener { v: View? -> onChangePasswordClick() }
        setPasswordControlsVisibility()
        passwordEnteredOnStart = OptionsFacade.ShortLivings[OptionsKeys.PasswordEntered] != null
    }

    private fun setPasswordControlsVisibility() {
        createPasswordControl!!.visibility = View.GONE
        enterPasswordControl!!.visibility = View.GONE
        changePasswordControl!!.visibility = View.GONE
        if (OptionsFacade.LongLivings[OptionsKeys.Password] != null) {
            if (OptionsFacade.ShortLivings[OptionsKeys.PasswordEntered] != null) changePasswordControl!!.visibility = View.VISIBLE else enterPasswordControl!!.visibility = View.VISIBLE
        } else createPasswordControl!!.visibility = View.VISIBLE
    }

    private fun onCreatePasswordClick() {
        val dialog = CreatePasswordDialog(
            this,
            IActionOneArgs { result: CreatePasswordDialog.Model ->
                OptionsFacade.LongLivings.addOrUpdate(arrayOf(Option(OptionsKeys.Password, result.password), Option(OptionsKeys.PasswordsHint, result.hint)))
                OptionsFacade.ShortLivings.addOrUpdate(arrayOf(Option(OptionsKeys.PasswordEntered, OptionsValues.True)))
                setPasswordControlsVisibility()
            },
            IActionZeroArgs {})
        dialog.show()
    }

    private fun onEnterPasswordClick() {
        val password = OptionsFacade.LongLivings[OptionsKeys.Password]
        val hint = OptionsFacade.LongLivings[OptionsKeys.PasswordsHint]
        val dialog = EnterPasswordDialog(
            this,
            IActionOneArgs { result: String ->
                if (result == password) {
                    OptionsFacade.ShortLivings.addOrUpdate(arrayOf(Option(OptionsKeys.PasswordEntered, OptionsValues.True)))
                    setPasswordControlsVisibility()
                } else ToastsHelper.Show(R.string.message_invalid_password, ToastsHelper.Position.Center)
            },
            IActionZeroArgs {}, hint!!)
        dialog.show()
    }

    private fun onChangePasswordClick() {
        val password = OptionsFacade.LongLivings[OptionsKeys.Password]
        val dialog = CreatePasswordDialog(
            this,
            IActionOneArgs { result: CreatePasswordDialog.Model -> OptionsFacade.LongLivings.addOrUpdate(arrayOf(Option(OptionsKeys.Password, result.password), Option(OptionsKeys.PasswordsHint, result.hint))) },
            IActionZeroArgs {}, R.string.dialog_change_password_title, R.layout.dialog_create_password, password!!)
        dialog.show()
    }

    override fun onBackPressed() {
        val passwordEnteredOnFinish = OptionsFacade.ShortLivings[OptionsKeys.PasswordEntered] != null
        val intent = Intent()
        intent.putExtra(ActivityResultCodes.IS_PASSWORD_ENTERED, !passwordEnteredOnStart && passwordEnteredOnFinish) // User enters or creates password
        setResult(RESULT_OK, intent)
        super.onBackPressed() // call finish() and close acivity
    }

    companion object {
        /**
         * Start activity
         */
        fun start(parentActivity: Activity) {
            val intent = Intent(parentActivity, MainOptionsActivity::class.java)
            parentActivity.startActivityForResult(intent, ActivityCodes.MAIN_OPTIONS)
        }

        /**
         * Parse result of activity
         */
        fun parseResult(data: Intent): Boolean {
            return data.getBooleanExtra(ActivityResultCodes.IS_PASSWORD_ENTERED, false)
        }
    }
}