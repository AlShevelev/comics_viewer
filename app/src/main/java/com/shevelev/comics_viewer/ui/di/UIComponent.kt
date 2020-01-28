package com.shevelev.comics_viewer.ui.di

import com.shevelev.comics_viewer.core.di_scopes.UIScope
import com.shevelev.comics_viewer.ui.activities.main.di.MainActivityComponent
import com.shevelev.comics_viewer.ui.activities.main_options.di.MainOptionsActivityComponent
import dagger.Subcomponent

@Subcomponent(
    modules = [
        UIModuleChilds::class
    ]
)
@UIScope
interface UIComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): UIComponent
    }

    val mainActivity: MainActivityComponent.Builder
    val mainOptionsActivity: MainOptionsActivityComponent.Builder
}