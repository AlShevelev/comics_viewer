package com.shevelev.comics_viewer.ui.activities.main_options.di

import com.shevelev.comics_viewer.core.di_scopes.ActivityScope
import com.shevelev.comics_viewer.ui.activities.main_options.MainOptionsActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [ ]
)
@ActivityScope
interface MainOptionsActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainOptionsActivityComponent
    }

    fun inject(activity: MainOptionsActivity)
}