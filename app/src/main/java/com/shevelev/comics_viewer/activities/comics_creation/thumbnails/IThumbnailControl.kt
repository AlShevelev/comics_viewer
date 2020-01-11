package com.shevelev.comics_viewer.activities.comics_creation.thumbnails

import android.graphics.drawable.Drawable

/**
 * Interface of control needed to load thumbnail
 */
interface IThumbnailControl {
    /**
     * Id if control (in most cases it's ListItemDrag::id)
     */
    val id: Int

    /**
     * Id of list in which this control hosted
     */
    val listId: ThumbnailListIds?

    /**
     * Full name of source file for thumbnail creation
     */
    val fullSourceFileName: String?

    /**
     * Get hashcode of image control
     */
    val imageHashCode: Int

    /**
     * Method to set thumbnail to a control
     */
    fun setThumbnail(thumbnail: Drawable?)
}