package com.shevelev.comics_viewer.ui.activities.folders

import android.view.Menu
import com.shevelev.comics_viewer.common.OptionsMenuManager

class ChooseFoldersOptionsMenuManager(menu: Menu) : OptionsMenuManager(menu) {
    fun setAcceptVisible(visible: Boolean) {
        setVisible(1, visible)
    }
}