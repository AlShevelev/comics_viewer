package com.shevelev.comics_viewer.common.helpers

import android.graphics.Paint
import android.graphics.Rect
import com.shevelev.comics_viewer.common.structs.Size

object StringsHelper {
    /**
     * Cut off file's extention
     */
    fun cutOffFileExt(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf(".")
        return if (lastDotIndex != -1) fileName.substring(0, lastDotIndex) else fileName
    }

    /**
     * Cut string to some length (by insering "..." in a middle of the source string)
     */
    fun cutToLength(source: String, totalLen: Int): String {
        val strToInsert = "..."
        val strToInsertLen = strToInsert.length
        val sourceLen = source.length
        if (source.length <= totalLen) return source
        if (totalLen <= strToInsertLen) return source.substring(0, strToInsertLen)
        val len = totalLen - strToInsertLen
        var headLen = len / 2
        val tailLen = headLen
        if (len % 2 != 0) headLen++
        return source.substring(0, headLen) +
            strToInsert +
            source.substring(sourceLen - tailLen, sourceLen)
    }

    /**
     * Cut text so its size can't be greate than some width
     * @param text  - text to cut
     * @param maxWidth
     * @param textPaint
     * @return
     */
    fun cutToSize(text: String, maxWidth: Int, textPaint: Paint): String {
        var text = text
        text = cutOffFileExt(text) // remove extention
        if (getTextSize(text, textPaint).width < maxWidth) // No need cut to size
            return text
        val minLen = 5
        val maxLen = 100
        var oldName = cutToLength(text, minLen)
        for (i in minLen + 1 until maxLen) {
            val newName = cutToLength(text, i)
            if (getTextSize(newName, textPaint).width > maxWidth) break
            oldName = newName
        }
        return oldName
    }

    /**
     * Calculate size of text
     * @param text - text to calculate
     * @param textPaint - text's painter
     * @return Calculated size
     */
    fun getTextSize(text: String, textPaint: Paint): Size {
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return Size(bounds.width(), bounds.height())
    }
}