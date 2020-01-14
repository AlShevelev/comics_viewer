package com.shevelev.comics_viewer.comics_workers

import android.graphics.Bitmap

interface IPreviewCreator {
    /**
     * Creates preview from source image in file
     * @param sourceFullNameOfFile
     * @return
     */
    fun createPreview(sourceFullNameOfFile: String): Bitmap

    /**
     * Creates preview from source image
     * @param sourceBitmap
     * @return
     */
    fun createPreview(sourceBitmap: Bitmap): Bitmap

    /**
     * Creates preview from source image and save its
     * @param sourceFullNameOfFile
     * @param previewFileName
     * @return created preview (null if fail)
     */
    fun createPreviewAndSave(sourceFullNameOfFile: String, previewFileName: String?): Bitmap?
}