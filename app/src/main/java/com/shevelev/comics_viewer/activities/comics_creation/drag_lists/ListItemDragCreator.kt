package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.shevelev.comics_viewer.App
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.StringsHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Create ListItemDrag from DiskItemInfo
 */
class ListItemDragCreator(private val viewSize: Size, private val textPaint: Paint) : IListItemDragCreatorImages {
    private companion object {
        const val ITEMS_ON_V_SCREEN = 3          // Quantity of items in vertical-oriented screen
        const val ITEMS_ON_H_SCREEN = 2           // Quantity of items in horizontal-oriented screen
    }

    override val stubImage : Drawable by lazy { calculatePageImage(BitmapsHelper.loadFromRaw(R.drawable.img_unloaded_page)) }

    /**
     * Create ListItemDrag without page image (with stub image)
     */
    fun create(diskItem: DiskItemInfo): ListItemDrag {
        val name = diskItem.displayName
        val briefName = cutName(name, viewSize.width / 2, textPaint)

        return ListItemDrag(diskItem.id, briefName, name, true, diskItem.fullname)
    }

    /**
     * Create page image. This method is very heavy so we should run it in background thread
     * @param fullBitmapFileName - full name (with path) of source bitmap file
     */
    override fun getPageImage(fullBitmapFileName: String?): Drawable? =
        calculatePageImage(BitmapFactory.decodeFile(fullBitmapFileName))

    /**
     * Calculate stub page
     */
    private fun calculatePageImage(sourceImage: Bitmap): Drawable {
        val newBitmapSize = calculateBitmapSize(Size(sourceImage.width, sourceImage.height), viewSize)
        val newBmp = Bitmap.createScaledBitmap(sourceImage, newBitmapSize.width, newBitmapSize.height, false)

        return BitmapDrawable(App.context!!.resources, newBmp)
    }

    /**
     * Get items quantity on screen
     */
    private fun  getItemsOnScreen(viewSize: Size): Int =
        if(viewSize.width < viewSize.height) ITEMS_ON_V_SCREEN else ITEMS_ON_H_SCREEN

    private fun calculateBitmapSize(currentBitmapSize: Size, viewSize: Size): Size {
        val itemsOnScreen=getItemsOnScreen(viewSize)

        val height=0.7f*(viewSize.height /itemsOnScreen)
        val width=currentBitmapSize.width *(height/currentBitmapSize.height)

        return Size(width.toInt(), height.toInt())
    }

    private fun cutName(name: String, maxWidth: Int, textPaint: Paint): String =
        StringsHelper.cutToSize(name, (maxWidth*0.9).toInt(), textPaint)
}
