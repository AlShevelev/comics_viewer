package com.shevelev.comics_viewer.di

import com.shevelev.comics_viewer.ui.di.UIComponent
import dagger.Module


@Module(subcomponents = [
    UIComponent::class
])
class AppModuleChilds