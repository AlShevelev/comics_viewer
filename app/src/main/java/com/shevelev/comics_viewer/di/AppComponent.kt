package com.shevelev.comics_viewer.di

import com.shevelev.comics_viewer.App
import com.shevelev.comics_viewer.core.di_scopes.ApplicationScope
import dagger.Component

@Component(modules = [
    AppModule::class,
    AppModuleBinds::class,
    AppModuleChilds::class
])
@ApplicationScope
interface AppComponent {
//    val ui: UIComponent.Builder

    fun inject(app: App)
}