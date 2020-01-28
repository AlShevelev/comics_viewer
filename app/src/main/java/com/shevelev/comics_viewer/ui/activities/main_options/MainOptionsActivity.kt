package com.shevelev.comics_viewer.ui.activities.main_options

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.shevelev.comics_viewer.App
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.ui.activities.ActivityBase
import com.shevelev.comics_viewer.ui.activities.ActivityCodes
import com.shevelev.comics_viewer.ui.activities.ActivityResultCodes
import com.shevelev.comics_viewer.ui.activities.main_options.di.MainOptionsActivityComponent
import javax.inject.Inject

class MainOptionsActivity : ActivityBase() {
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

    private var createPasswordControl: LinearLayout? = null
    private var enterPasswordControl: LinearLayout? = null
    private var changePasswordControl: LinearLayout? = null
    private var passwordEnteredOnStart = false

    @Inject
    internal lateinit var keyValueStorage: KeyValueStorageFacade

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
        passwordEnteredOnStart = keyValueStorage.getPasswordEntered() != null
    }

    override fun inject(key: String) {
        App.injections.get<MainOptionsActivityComponent>(key).inject(this)
    }

    override fun releaseInjection(key: String) {
        App.injections.release<MainOptionsActivityComponent>(key)
    }

    private fun setPasswordControlsVisibility() {
        createPasswordControl!!.visibility = View.GONE
        enterPasswordControl!!.visibility = View.GONE
        changePasswordControl!!.visibility = View.GONE

        if (keyValueStorage.getPassword() != null) {
            if (keyValueStorage.getPasswordEntered() != null) {
                changePasswordControl!!.visibility = View.VISIBLE
            } else {
                enterPasswordControl!!.visibility = View.VISIBLE
            }
        } else {
            createPasswordControl!!.visibility = View.VISIBLE
        }
    }

    private fun onCreatePasswordClick() {
        val dialog = CreatePasswordDialog(
            this,
            { result: CreatePasswordDialog.Model ->
                keyValueStorage.savePassword(result.password)
                keyValueStorage.savePasswordHint(result.hint)
                keyValueStorage.savePasswordEntered(true)
                setPasswordControlsVisibility()
            },
            { })
        dialog.show()
    }

    private fun onEnterPasswordClick() {
        val password = keyValueStorage.getPassword()
        val hint = keyValueStorage.getPasswordHint()
        val dialog = EnterPasswordDialog(
            this,
            { result: String ->
                if (result == password) {
                    keyValueStorage.savePasswordEntered(true)
                    setPasswordControlsVisibility()
                } else ToastsHelper.Show(this, R.string.message_invalid_password, ToastsHelper.Position.Center)
            },
            { }, hint!!)
        dialog.show()
    }

    private fun onChangePasswordClick() {
        val password = keyValueStorage.getPassword()
        val dialog = CreatePasswordDialog(
            this,
            { result: CreatePasswordDialog.Model ->
                keyValueStorage.savePassword(result.password)
                keyValueStorage.savePasswordHint(result.hint)
            },
            { }, R.string.dialog_change_password_title, R.layout.dialog_create_password, password!!)
        dialog.show()
    }

    override fun onBackPressed() {
        val passwordEnteredOnFinish = keyValueStorage.getPasswordEntered() != null
        val intent = Intent()
        intent.putExtra(ActivityResultCodes.IS_PASSWORD_ENTERED, !passwordEnteredOnStart && passwordEnteredOnFinish) // User enters or creates password
        setResult(RESULT_OK, intent)
        super.onBackPressed() // call finish() and close acivity
    }
}