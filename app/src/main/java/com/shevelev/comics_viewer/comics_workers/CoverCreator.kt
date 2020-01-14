package com.shevelev.comics_viewer.comics_workers

import android.graphics.*
import com.shevelev.comics_viewer.App.Main.context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Crete cover logic
 */
object CoverCreator {
    /**
     * Create cover bitmap from file
     * @param fullNameToFile - path to file with source bitmap
     * @return
     */
    fun create(fullNameToFile: String?, previewCreator: IPreviewCreator): Bitmap {
        val coverBitmapScaled = previewCreator.createPreview(fullNameToFile!!) // Scale source bitmap
        return drawCover(coverBitmapScaled) // and setDiskItems cover
    }

    /**
     * Create cover bitmap from slaledBitmap
     * @param sourceBitmapScaled - scaled bitmap
     * @return
     */
    fun create(sourceBitmapScaled: Bitmap?, previewCreator: IPreviewCreator): Bitmap {
        val coverBitmapScaled = previewCreator.createPreview(sourceBitmapScaled!!) // Scale source bitmap
        return drawCover(coverBitmapScaled)
    }

    /**
     * Calculate size of shadow (value1 is same as value2)
     */
    private fun calculateShadowSize(bitmapSize: Size): Int {
        val result = (bitmapSize.width + bitmapSize.height) / (2f * 15.48f)
        val shadowScaleFactor = 1.2f
        return (result * shadowScaleFactor).toInt() // we must *shadowScaleFactor becouse cubic curve is used to draw shadod
    }

    /**
     * Calculate total size of cover (with bitmap and shadow)
     * @param bitmap
     * @return
     */
    private fun calculateCoverSize(bitmap: Bitmap, shadowSize: Int): Size {
        val resultWidth = bitmap.width + shadowSize / 2
        var resultHeight = bitmap.height
        if (resultWidth > resultHeight) resultHeight = resultWidth
        return Size(resultWidth, resultHeight)
    }

    /**
     * Draw cover
     * @param bitmap bitmap to draw
     * @return
     */
    private fun drawCover(bitmap: Bitmap): Bitmap {
        val bitmapSize = Size(bitmap.width, bitmap.height)
        val shadowSize = calculateShadowSize(bitmapSize)
        val coverSize = calculateCoverSize(bitmap, shadowSize)
        val bmOverlay = Bitmap.createBitmap(coverSize.width, coverSize.height, Bitmap.Config.ARGB_8888)
        bmOverlay.eraseColor(Color.TRANSPARENT) // Make transparent
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(bitmap, 0f, coverSize.height - bitmap.height.toFloat(), null) // Place bitmap on bottom
        drawShadow(canvas, bitmapSize, coverSize, shadowSize) // Draw shadow
        return bmOverlay
    }

    private fun drawShadow(canvas: Canvas, bitmapSize: Size, coverSize: Size, shadowSize: Int) {
        val paint = Paint() // draw shadow - in separate method
        paint.color = context!!.resources.getColor(R.color.bookcase_cover_shadow)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        val a = Point(bitmapSize.width, coverSize.height)
        val b = Point(a.x + shadowSize, a.y - shadowSize)
        val c = Point(a.x, coverSize.height - bitmapSize.height)
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(a.x.toFloat(), a.y.toFloat())
        path.quadTo(b.x.toFloat(), b.y.toFloat(), c.x.toFloat(), c.y.toFloat()) // Use cubic curve to avoid sharp angle of shadow (it's to rough)
        path.lineTo(a.x.toFloat(), a.y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}