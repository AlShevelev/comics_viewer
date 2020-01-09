package com.shevelev.comics_viewer.activities.comics_creation.thumbnails;

import android.graphics.drawable.Drawable;

/**
 * Inteface of control needed to load thumbnail
 */
public interface IThumbnailControl
{
    /**
     * Id if control (in most cases it's ListItemDrag::id)
     */
    int getId();

    /**
     * Id if list in which this control hosts
     */
    ThumbnailListIds getListId();

    /**
     * Full name of source file for thumbnail creation
     */
    String getFullSourceFileName();

    /**
     * Get hashcode of image control
     */
    int getImageHachCode();

    /**
     * Method to set thumbnail to control
     */
    void setThumbnail(Drawable thumbnail);
}
