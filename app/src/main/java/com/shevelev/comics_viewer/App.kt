package com.shevelev.comics_viewer

import android.app.Application
import android.content.Context
import com.activeandroid.ActiveAndroid

/**
 * Application configuration class
 */
class App : Application() {
    companion object Main {
        var context: Context? = null
            private set

        fun getResourceString(resourceId: Int): String {
            return context!!.resources.getString(resourceId)
        }
    }

    override fun onCreate() {
        super.onCreate()
        ActiveAndroid.initialize(this)
        context = applicationContext
    }
}