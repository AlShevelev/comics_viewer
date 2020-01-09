package com.shevelev.comics_viewer.activities.comics_creation.thumbnails;

import android.graphics.drawable.Drawable;

public class ThumbnailCalculationResult
{
    private final int id;
    public int getId() { return id; }

    /**
     * Calculated image
     */
    private final Drawable pageImage;
    public Drawable getPageImage() { return pageImage; }

    public ThumbnailCalculationResult(int id, Drawable pageImage)
    {
        this.id = id;
        this.pageImage = pageImage;
    }
}
