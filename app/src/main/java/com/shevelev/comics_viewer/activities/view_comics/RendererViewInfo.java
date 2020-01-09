package com.shevelev.comics_viewer.activities.view_comics;

import android.graphics.RectF;

import com.shevelev.comics_viewer.common.structs.SizeF;

/**
 * Information about renderer view
 */
public class RendererViewInfo
{
    private RectF viewRect = new RectF();       // View rect [units]
    private SizeF viewAreaSize;         // Size of view area [px]

    public RendererViewInfo(RectF viewRect, SizeF viewAreaSize)
    {
        this.viewRect = viewRect;
        this.viewAreaSize = viewAreaSize;
    }

    public RectF getViewRect()
    {
        return viewRect;
    }

    public SizeF getViewAreaSize()
    {
        return viewAreaSize;
    }
}
