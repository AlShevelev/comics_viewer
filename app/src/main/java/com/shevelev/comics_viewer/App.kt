package com.shevelev.comics_viewer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.activeandroid.ActiveAndroid
import com.shevelev.comics_viewer.di.AppComponent
import com.shevelev.comics_viewer.di_storage.DependencyInjectionStorage
import com.shevelev.comics_viewer.utils.id.IdUtil

/**
 * Application configuration class
 */
class App : Application() {
    companion object Main {
        @SuppressLint("StaticFieldLeak")
        lateinit var injections : DependencyInjectionStorage
            private set
    }

    override fun onCreate() {
        super.onCreate()

        injections = DependencyInjectionStorage(applicationContext)
        injections.get<AppComponent>(IdUtil.generateStringId()).inject(this)

        ActiveAndroid.initialize(this)
    }
}