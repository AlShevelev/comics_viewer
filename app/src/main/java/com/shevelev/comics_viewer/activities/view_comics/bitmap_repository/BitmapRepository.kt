package com.shevelev.comics_viewer.activities.view_comics.bitmap_repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shevelev.comics_viewer.activities.view_comics.ResizingState
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.dal.dto.Page
import java.util.*

/**
 * Caches one bitmap of max size per page
 */
class BitmapRepository(private val context: Context, pages: List<Page>) : IBitmapRepository {
    // list of comics pages
    private val pages : List<Page>

    // Indexes of cached bitmaps
    private val indexesQueue : QueueWithDisplacement

    private val cachedBitmaps: MutableMap<Int, Bitmap>

    override val pageCount: Int
        get() = pages.size

    /**
     * @param index index of page
     * @return
     */
    override fun getByIndex(index: Int, viewAreaWidth: Int, viewAreaHeight: Int): Bitmap {
        if (indexesQueue.isExists(index)) // get from cache
            return cachedBitmaps[index]!!
        val displacementIndex = indexesQueue.push(index)
        if (displacementIndex != null) // remove old value from cache
            cachedBitmaps.remove(displacementIndex)
        val bitmap = loadBitmap(index, viewAreaWidth, viewAreaHeight)
        cachedBitmaps[index] = bitmap
        return bitmap
    }

    private fun loadBitmap(index: Int, viewAreaWidth: Int, viewAreaHeight: Int): Bitmap {
        val fullName = AppPrivateFilesHelper.getFullName(context, pages[index].fileName!!)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(fullName, options) // Read image size only
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        if (imageHeight <= viewAreaHeight || imageWidth <= viewAreaWidth) // Image is in-screen - decode it without scaling
            return BitmapFactory.decodeFile(fullName)
        val viewAreaMaxWidth = (viewAreaWidth * ResizingState.MAX_SCALE).toInt()
        val viewAreaMaxHeight = (viewAreaHeight * ResizingState.MAX_SCALE).toInt() // Max possible image size
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateInSampleSize(imageWidth, imageHeight, viewAreaMaxWidth, viewAreaMaxHeight)
        return BitmapFactory.decodeFile(fullName, options)
    }

    companion object {
        private const val MAX_INDEXES_TO_STORE = 3
        /**
         * Calculate scaling factor (as power of 2)
         */
        fun calculateInSampleSize(imageWidth: Int, imageHeight: Int, viewAreaMaxWidth: Int, viewAreaMaxHeight: Int): Int {
            var inSampleSize = 1
            if (imageHeight > viewAreaMaxHeight || imageWidth > viewAreaMaxWidth) {
                val halfHeight = imageHeight / 2
                val halfWidth = imageWidth / 2
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
// height and width larger than the requested height and width.
                while (halfHeight / inSampleSize > viewAreaMaxHeight || (halfHeight / inSampleSize).toFloat() / viewAreaMaxHeight.toFloat() > 0.7f ||
                    halfWidth / inSampleSize > viewAreaMaxHeight || (halfWidth / inSampleSize).toFloat() / viewAreaMaxHeight.toFloat() > 0.7f) inSampleSize *= 2
            }
            return inSampleSize
        }
    }

    init {
        indexesQueue = QueueWithDisplacement(MAX_INDEXES_TO_STORE)
        cachedBitmaps = TreeMap()
        this.pages = pages
    }
}