package com.shevelev.comics_viewer.comics_workers;

import android.graphics.Bitmap;

import com.shevelev.comics_viewer.common.helpers.BitmapsHelper;
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper;
import com.shevelev.comics_viewer.common.helpers.files.BitmapsQuality;
import com.shevelev.comics_viewer.common.structs.Size;

/**
 * Create preview bitmap from source image
 */
public class PreviewCreator implements IPreviewCreator
{
    private final Size clientSize;          // Size of activity client area

    public PreviewCreator(Size clientSize)
    {
        this.clientSize = clientSize;
    }

    /**
     * Creates preview from source image in file
     * @param sourceFullNameOfFile
     * @return
     */
    @Override
    public Bitmap createPreview(String sourceFullNameOfFile)
    {
        Bitmap coverBitmapSource= BitmapsHelper.loadFromFile(sourceFullNameOfFile);           // load source file
        return createPreview(coverBitmapSource);
    }

    /**
     * Creates preview from source image
     * @param sourceBitmap
     * @return
     */
    @Override
    public Bitmap createPreview(Bitmap sourceBitmap)
    {
        Size sourceSize=new Size(sourceBitmap.getWidth(), sourceBitmap.getHeight());  // Get its size
        Bitmap coverBitmapScaled = BitmapsHelper.scale(sourceBitmap, calculateScale(clientSize, sourceSize));     // Scale source bitmap

        return coverBitmapScaled;
    }

    /**
     * Calculate scale factor for source bitmap
     * @param clientSize - size of activity client area
     * @param imageToScale
     * @return
     */
    private Size calculateScale(Size clientSize, Size imageToScale)
    {
        int clientMaxSide=Math.max(clientSize.getWidth(), clientSize.getHeight());

        float maxHeight=clientMaxSide/3f;

        float scaleFactor=maxHeight/(float)imageToScale.getHeight();

        return new Size((int)(imageToScale.getWidth()*scaleFactor), (int)maxHeight);
    }

    /**
     * Get file name for the preview image
     * @param fileName source image's name
     * @return
     */
    public static String getPreviewFileName(String fileName)
    {
        if(fileName==null)
            return null;
        return "p"+fileName;
    }

    /**
     * Creates preview from source image and save its
     * @param sourceFullNameOfFile
     * @param previewFileName
     * @return true - preview was created and saved
     */
    public Bitmap createPreviewAndSave(String sourceFullNameOfFile, String previewFileName)
    {
        Bitmap previewBitmap=createPreview(sourceFullNameOfFile);

        if(AppPrivateFilesHelper.createFromBitmap(          // Save to disk
                previewFileName,
                previewBitmap,
                BitmapsQuality.Low,
                Bitmap.CompressFormat.JPEG))
            return previewBitmap;
        return null;
    }
}
