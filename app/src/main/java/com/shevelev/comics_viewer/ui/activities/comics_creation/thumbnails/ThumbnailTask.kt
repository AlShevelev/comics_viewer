package com.shevelev.comics_viewer.ui.activities.comics_creation.thumbnails

import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskBase

/**
 * Task to calculate thumbnail
 * [fullPathToImageFile] Full path to image to calculate
 */
class ThumbnailTask(
    id: Int,
    val listIds: ThumbnailListIds,
    val fullPathToImageFile: String
) : ProducerConsumerTaskBase(id)