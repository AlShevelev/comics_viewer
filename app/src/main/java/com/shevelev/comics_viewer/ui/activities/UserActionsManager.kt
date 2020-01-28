package com.shevelev.comics_viewer.ui.activities

import android.app.Activity
import android.view.WindowManager

/**
 * Turn on/off user actions
 */
class UserActionsManager(private val activity: Activity) {
    var isActionsBlocked = false // Flag for blocking on-screen buttons
        private set

    fun lock() {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        isActionsBlocked = true
    }

    fun unlock() {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        isActionsBlocked = false
    }
}