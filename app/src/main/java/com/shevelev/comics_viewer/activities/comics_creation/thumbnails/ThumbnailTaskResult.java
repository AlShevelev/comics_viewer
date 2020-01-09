package com.shevelev.comics_viewer.activities.comics_creation.thumbnails;

import android.graphics.drawable.Drawable;

import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskProcessingResultBase;

/**
 * Thumbnail calculation result
 */
public class ThumbnailTaskResult extends ProducerConsumerTaskProcessingResultBase
{
    private final ThumbnailListIds listIds;
    public ThumbnailListIds getListIds() { return listIds; }

    /**
     * Calculated image
     */
    private final Drawable pageImage;
    public Drawable getPageImage() { return pageImage; }

    public ThumbnailTaskResult(int id, ThumbnailListIds listIds, Drawable pageImage)
    {
        super(id);
        this.pageImage = pageImage;
        this.listIds = listIds;
    }
}
