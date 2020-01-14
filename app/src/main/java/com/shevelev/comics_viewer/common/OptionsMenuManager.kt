package com.shevelev.comics_viewer.common

import android.view.Menu
import android.view.MenuItem

open class OptionsMenuManager(menu: Menu) {
    private val allMenuItems: Array<MenuItem?>
    fun setVisible(visible: Boolean) {
        for (i in allMenuItems.indices) allMenuItems[i]!!.isVisible = visible
    }

    protected fun setVisible(menuItemIndex: Int, visible: Boolean) {
        allMenuItems[menuItemIndex]!!.isVisible = visible
    }

    init {
        allMenuItems = arrayOfNulls(menu.size())
        for (i in allMenuItems.indices) allMenuItems[i] = menu.getItem(i)
    }
}