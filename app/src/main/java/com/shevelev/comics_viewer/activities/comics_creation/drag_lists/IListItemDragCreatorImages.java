package com.shevelev.comics_viewer.activities.comics_creation.drag_lists;

import android.graphics.drawable.Drawable;

/**
 * Interface to working with images
 */
public interface IListItemDragCreatorImages
{
    Drawable getPageImage(String fullBitmapFileName);

    Drawable getStubPageImage();
}
