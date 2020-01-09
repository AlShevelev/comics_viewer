package com.shevelev.comics_viewer.activities.comics_creation.drag_lists;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.App;
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo;
import com.shevelev.comics_viewer.common.structs.Size;
import com.shevelev.comics_viewer.common.helpers.StringsHelper;

/**
 * Create ListItemDrag from DiskItemInfo
 */
public class ListItemDragCreator implements IListItemDragCreatorImages
{
    private static final int ITEMS_ON_V_SCREEN=3;           // Quantity of items in vertical-orientatios screen
    private static final int ITEMS_ON_H_SCREEN=2;           // Quantity of items in horizontal-orientatios screen

    private final Size viewSize;
    private final Paint textPaint;

    private Drawable stubPageImage;         // Stub image of page

    public ListItemDragCreator(Size viewSize, Paint textPaint)
    {
        this.viewSize = viewSize;
        this.textPaint = textPaint;

        getStubPageImage();
    }

    /**
     * Create ListItemDrag without page image (with stub image)
     */
    public ListItemDrag create(DiskItemInfo diskItem)
    {
        String name=diskItem.getDisplayName();
        String cutedName = cutName(name, viewSize.getWidth() / 2, textPaint);

        return new ListItemDrag(diskItem.getId(), cutedName, name, true, diskItem.getFullname());
    }

    /**
     * Create page image. This method is very heavy so we should run it in background thread
     * @param fullBitmapFileName - full name (with path) of source bitmap file
     */
    @Override
    public Drawable getPageImage(String fullBitmapFileName)
    {
        return calculatePageImage(BitmapFactory.decodeFile(fullBitmapFileName));
    }

    /**
     * Calculate stub page
     */
    @Override
    public Drawable getStubPageImage()
    {
        if(stubPageImage==null)
            stubPageImage = calculatePageImage(BitmapsHelper.loadFromRaw(R.raw.unloaded_page));
        return stubPageImage;
    }

    private Drawable calculatePageImage(Bitmap sourceImage)
    {
        Size newBitmapSize=calculateBitmapSize(new Size(sourceImage.getWidth(), sourceImage.getHeight()), viewSize);
        Bitmap newBmp=Bitmap.createScaledBitmap(sourceImage, newBitmapSize.getWidth(), newBitmapSize.getHeight(), false);

        return new BitmapDrawable(App.getContext().getResources(), newBmp);
    }

    /**
     * Get items quantity on screen
     */
    private int getItemsOnScreen(Size viewSize){
        if(viewSize.getWidth() < viewSize.getHeight())
            return ITEMS_ON_V_SCREEN;
        return ITEMS_ON_H_SCREEN;
    }

    private Size calculateBitmapSize(Size currentBitmapSize, Size viewSize)
    {
        int itemsOnScreen=getItemsOnScreen(viewSize);

        float height=0.7f*(viewSize.getHeight()/itemsOnScreen);
        float width=currentBitmapSize.getWidth()*(height/currentBitmapSize.getHeight());

        return new Size((int)width, (int)height);
    }

    private String cutName(String name, int maxWidth, Paint textPaint) {
        maxWidth*=0.9;
        return StringsHelper.cutToSize(name, maxWidth, textPaint);
    }
}