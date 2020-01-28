package com.shevelev.comics_viewer.ui.activities.main.bookshelf

import android.widget.ImageView
import android.widget.LinearLayout

/**
 * ViewHolders for BookshelfListAdapter
 */
class ViewHolders {
    class ShelfHolder {
        var loverShelf: ImageView? = null
        var comicsContainer: LinearLayout? = null
    }

    class PagesHolder {
        var pagesControl: LinearLayout? = null
    }
}