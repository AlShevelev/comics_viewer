package com.shevelev.comics_viewer.ui.activities.main.bookshelf.comics_control

import android.view.ContextMenu.ContextMenuInfo

class ComicsContextMenuInfo : ContextMenuInfo {
    var dbId: Long = 0
    lateinit var menuTitle: String
}