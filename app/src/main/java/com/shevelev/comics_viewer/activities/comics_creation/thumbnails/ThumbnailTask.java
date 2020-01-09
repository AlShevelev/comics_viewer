package com.shevelev.comics_viewer.activities.comics_creation.thumbnails;

import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskBase;

/**
 * Task to calculate thumbnail
 */
public class ThumbnailTask extends ProducerConsumerTaskBase
{
    private final ThumbnailListIds listIds;
    public ThumbnailListIds getListIds() { return listIds; }

    /**
     * Full path to image to calculate
     */
    private final String fullPathToImageFile;
    public String getFullPathToImageFile() { return fullPathToImageFile; }

    public ThumbnailTask(int id, ThumbnailListIds listIds, String fullPathToImageFile)
    {
        super(id);
        this.fullPathToImageFile = fullPathToImageFile;
        this.listIds = listIds;
    }
}
