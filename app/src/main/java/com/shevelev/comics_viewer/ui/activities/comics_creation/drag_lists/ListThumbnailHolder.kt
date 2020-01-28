package com.shevelev.comics_viewer.ui.activities.comics_creation.drag_lists

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.shevelev.comics_viewer.ui.activities.comics_creation.thumbnails.IThumbnailControl
import com.shevelev.comics_viewer.ui.activities.comics_creation.thumbnails.ThumbnailListIds

/**
 * [id] Id if control (in most cases it's ListItemDrag::id)
 * [listId] Id of list in which this control hosted
 * [fullSourceFileName] Full name of source file for thumbnail creation
 */
class ListThumbnailHolder(
    override val id: Int,
    override val listId: ThumbnailListIds?,
    override val fullSourceFileName: String?,
    private val pageImage: ImageView
) : IThumbnailControl {

    /**
     * Get hashcode of image control
     */
    override val imageHashCode
        get() = pageImage.hashCode()

    /**
     * Method to set thumbnail to a control
     */
    override fun setThumbnail(thumbnail: Drawable?) = pageImage.setImageDrawable(thumbnail)
}