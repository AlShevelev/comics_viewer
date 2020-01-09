package com.shevelev.comics_viewer.activities.view_comics.bitmap_repository;

import android.graphics.Bitmap;

/**
 * Created by Syleiman on 26.11.2015.
 */
public interface IBitmapRepository
{
    /**
     *
     * @param index index of page
     * @return
     */
    Bitmap getByIndex(int index, int viewAreaWidth, int viewAreaHeight);

    int getPageCount();
}
