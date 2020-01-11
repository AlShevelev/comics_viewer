package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.graphics.drawable.Drawable

/**
 * Interface to working with images
 */
interface IListItemDragCreatorImages {
    val stubImage: Drawable

    fun getPageImage(fullBitmapFileName: String?): Drawable?
}