package com.shevelev.comics_viewer.di_storage

import android.content.Context
import com.shevelev.comics_viewer.di.AppComponent
import com.shevelev.comics_viewer.di.AppModule
import com.shevelev.comics_viewer.di.DaggerAppComponent
import com.shevelev.comics_viewer.ui.activities.main.di.MainActivityComponent
import com.shevelev.comics_viewer.ui.activities.main_options.di.MainOptionsActivityComponent
import com.shevelev.comics_viewer.ui.di.UIComponent
import com.shevelev.comics_viewer.utils.id.IdUtil
import kotlin.reflect.KClass

/** Storage for Dagger components on application level  */
class DependencyInjectionStorage(private val appContext: Context) {

    private val components = mutableMapOf<KClass<*>, MutableMap<String, Any>>()

    inline fun <reified T> get(key: String, vararg args: Any?): T = getComponent(key, T::class, args)

    inline fun <reified T> release(key: String) = releaseComponent(key, T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(key: String, type: KClass<*>, args: Array<out Any?>): T {
        val componentsSet = components[type]

        return if(componentsSet == null) {
            val component = provideComponent<T>(type, args)
            components[type] = mutableMapOf(key to component as Any)

            component
        } else {
            var component = componentsSet[key]
            if(component != null) {
                component as T
            }

            component = provideComponent<T>(type, args)
            componentsSet[key] = component as Any
            component
        }
    }

    fun releaseComponent(key: String, type: KClass<*>) {
        val componentsSet = components[type] ?: return

        componentsSet.remove(key)

        if(componentsSet.isEmpty()) {
            components.remove(type)
        }
    }

    inline fun <reified T> getBase(): T = getBaseComponent(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T> getBaseComponent(type: KClass<*>): T {
        val componentsSet = components[type]

        return if(componentsSet != null) {
            componentsSet.entries.first().value as T
        } else {
            val component = provideComponent<T>(type, arrayOfNulls(0))
            components[type] = mutableMapOf(IdUtil.generateStringId() to component as Any)

            component
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun <T> provideComponent(type: KClass<*>, args: Array<out Any?>): T {
        @Suppress("EXPERIMENTAL_API_USAGE")
        return when (type) {
            AppComponent::class -> DaggerAppComponent.builder().appModule(AppModule(appContext)).build()

            UIComponent::class -> getBase<AppComponent>().ui.build()

            MainActivityComponent::class -> getBase<UIComponent>().mainActivity.build()

            MainOptionsActivityComponent::class -> getBase<UIComponent>().mainOptionsActivity.build()

            else -> throw UnsupportedOperationException("This component is not supported: ${type.simpleName}")
        } as T
    }
}