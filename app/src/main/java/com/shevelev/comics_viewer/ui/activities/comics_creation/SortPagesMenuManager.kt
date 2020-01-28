package com.shevelev.comics_viewer.ui.activities.comics_creation

import android.view.Menu
import com.shevelev.comics_viewer.common.OptionsMenuManager

class SortPagesMenuManager(menu: Menu) : OptionsMenuManager(menu) {
    fun setAcceptVisible(visible: Boolean) {
        setVisible(0, visible)
    }
}