package com.shevelev.comics_viewer.activities.comics_creation.thumbnails

import android.graphics.drawable.Drawable
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskProcessingResultBase

/**
 * Thumbnail calculation result
 * [pageImage] Calculated image
 */
class ThumbnailTaskResult(
    id: Int,
    val listIds: ThumbnailListIds,
    val pageImage: Drawable
) : ProducerConsumerTaskProcessingResultBase(id)