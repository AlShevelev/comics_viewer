package com.shevelev.comics_viewer.common.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.shevelev.comics_viewer.common.structs.AreaF
import com.shevelev.comics_viewer.common.structs.Size

object BitmapsHelper {
    /**
     * Is bitmap dark?
     * Idea (but not code :) ) was got from here: https://gist.github.com/brwnx/191c79b6c2b3befbfc7d
     * @return BitmapDarkRate
     */
    fun isDark(bitmap: Bitmap, areaToCheck: AreaF): BitmapDarkRate {
        val checkedArea = areaToCheck.toArea(Size(bitmap.width, bitmap.height))
        val dark = false
        val totalPixels = checkedArea.size.width * checkedArea.size.height.toFloat()
        val darkThreshold = totalPixels * 0.5f
        var darkPixels = 0
        val pixels = IntArray(checkedArea.size.width * checkedArea.size.height)
        bitmap.getPixels(
            pixels,
            0,
            checkedArea.size.width,
            checkedArea.leftTop.left,
            checkedArea.leftTop.top,
            checkedArea.size.width,
            checkedArea.size.height)
        for (pixel in pixels) {
            val r = Color.red(pixel)
            val g = Color.green(pixel)
            val b = Color.blue(pixel)
            val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
            if (luminance < 150) darkPixels++
        }
        /*
        if(Math.abs(darkPixels-darkThreshold)<totalPixels*0.15f)            // So-so
            return BitmapDarkRate.Gray;
*/return if (darkPixels >= darkThreshold) BitmapDarkRate.Dark else BitmapDarkRate.Light
    }

    fun loadFromFile(fullFileName: String?): Bitmap {
        return BitmapFactory.decodeFile(fullFileName)
    }

    fun loadFromRaw(context: Context, rawResourceId: Int): Bitmap {
        return BitmapFactory.decodeStream(context.resources.openRawResource(rawResourceId))
    }

    fun scale(bmp: Bitmap?, targetSize: Size): Bitmap {
        return Bitmap.createScaledBitmap(bmp, targetSize.width, targetSize.height, false)
    }
}