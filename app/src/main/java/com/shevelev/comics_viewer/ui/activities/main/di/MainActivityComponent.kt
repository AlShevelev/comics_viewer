package com.shevelev.comics_viewer.ui.activities.main.di

import com.shevelev.comics_viewer.core.di_scopes.ActivityScope
import com.shevelev.comics_viewer.ui.activities.main.MainActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [ ]
)
@ActivityScope
interface MainActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}