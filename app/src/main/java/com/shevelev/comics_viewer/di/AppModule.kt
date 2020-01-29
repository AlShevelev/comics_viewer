package com.shevelev.comics_viewer.di

import android.content.Context
import com.shevelev.comics_viewer.core.di_scopes.ApplicationScope
import com.shevelev.comics_viewer.core.shared_interfaces.DispatchersProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Application level module - global objects are created here   */
@Module
class AppModule(private val appContext: Context) {

    @Provides
    @ApplicationScope
    internal fun provideContext(): Context = appContext

    @Provides
    internal fun provideDispatchersProvider(): DispatchersProvider = object : DispatchersProvider {
        override val uiDispatcher: CoroutineDispatcher
            get() = Dispatchers.Main
        override val calculationsDispatcher: CoroutineDispatcher
            get() = Dispatchers.Default
        override val ioDispatcher: CoroutineDispatcher
            get() = Dispatchers.IO
    }
}