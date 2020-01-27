package com.shevelev.comics_viewer.comics_workers

import android.content.Context
import android.graphics.Bitmap
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.common.helpers.files.BitmapsQuality
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Create preview bitmap from source image
 */
class PreviewCreator(
    private val context: Context,
    private val clientSize: Size            // Size of activity client area
) : IPreviewCreator {

    /**
     * Creates preview from source image in file
     * @param sourceFullNameOfFile
     * @return
     */
    override fun createPreview(sourceFullNameOfFile: String): Bitmap {
        val coverBitmapSource = BitmapsHelper.loadFromFile(sourceFullNameOfFile) // load source file
        return createPreview(coverBitmapSource)
    }

    /**
     * Creates preview from source image
     * @param sourceBitmap
     * @return
     */
    override fun createPreview(sourceBitmap: Bitmap): Bitmap {
        val sourceSize = Size(sourceBitmap.width, sourceBitmap.height) // Get its size
        return BitmapsHelper.scale(sourceBitmap, calculateScale(clientSize, sourceSize))
    }

    /**
     * Calculate scale factor for source bitmap
     * @param clientSize - size of activity client area
     * @param imageToScale
     * @return
     */
    private fun calculateScale(clientSize: Size, imageToScale: Size): Size {
        val clientMaxSide = Math.max(clientSize.width, clientSize.height)
        val maxHeight = clientMaxSide / 3f
        val scaleFactor = maxHeight / imageToScale.height.toFloat()
        return Size((imageToScale.width * scaleFactor).toInt(), maxHeight.toInt())
    }

    /**
     * Creates preview from source image and save its
     * @param sourceFullNameOfFile
     * @param previewFileName
     * @return true - preview was created and saved
     */
    override fun createPreviewAndSave(sourceFullNameOfFile: String, previewFileName: String?): Bitmap? {
        val previewBitmap = createPreview(sourceFullNameOfFile)
        return if (AppPrivateFilesHelper.createFromBitmap( // Save to disk
                context,
                previewFileName,
                previewBitmap,
                BitmapsQuality.Low,
                Bitmap.CompressFormat.JPEG)) previewBitmap else null
    }

    companion object {
        /**
         * Get file name for the preview image
         * @param fileName source image's name
         * @return
         */
        fun getPreviewFileName(fileName: String?): String? {
            return if (fileName == null) null else "p$fileName"
        }
    }

}