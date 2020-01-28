package com.shevelev.comics_viewer.ui.activities

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.comics_viewer.utils.id.IdUtil

abstract class ActivityBase : AppCompatActivity() {
    companion object {
        private const val INJECTION_KEY = "INJECTION_KEY"
    }

    private lateinit var injectionKey: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectionKey = savedInstanceState?.getString(INJECTION_KEY) ?: IdUtil.generateStringId()
        inject(injectionKey)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(INJECTION_KEY, injectionKey)
        super.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onDestroy() {
        if(isFinishing) {
            releaseInjection(injectionKey)
        }
        super.onDestroy()
    }

    protected abstract fun inject(key: String)

    protected abstract fun releaseInjection(key: String)
}