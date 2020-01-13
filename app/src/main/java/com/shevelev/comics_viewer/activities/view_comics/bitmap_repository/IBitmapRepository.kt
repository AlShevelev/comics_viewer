package com.shevelev.comics_viewer.activities.view_comics.bitmap_repository

import android.graphics.Bitmap

/**
 * Created by Syleiman on 26.11.2015.
 */
interface IBitmapRepository {
    /**
     *
     * @param index index of page
     * @return
     */
    fun getByIndex(index: Int, viewAreaWidth: Int, viewAreaHeight: Int): Bitmap?

    val pageCount: Int
}