package com.shevelev.comics_viewer.activities.comics_creation;

import android.content.Context;
import android.widget.RelativeLayout;

import com.shevelev.comics_viewer.common.structs.Size;

/**
 * Contecst view for ComicsCreationActivity
 */
public class SortPagesActivityView extends RelativeLayout
{
    public SortPagesActivityView(Context context, int layoutId)
    {
        super(context);

        inflate(context, layoutId, this);
    }

    public Size getSize()
    {
        return new Size(getWidth(), getHeight());
    }
}
