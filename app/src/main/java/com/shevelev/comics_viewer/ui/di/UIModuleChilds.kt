package com.shevelev.comics_viewer.ui.di

import com.shevelev.comics_viewer.ui.activities.main.di.MainActivityComponent
import com.shevelev.comics_viewer.ui.activities.main_options.di.MainOptionsActivityComponent
import dagger.Module

@Module(subcomponents = [
    MainActivityComponent::class,
    MainOptionsActivityComponent::class
])
class UIModuleChilds