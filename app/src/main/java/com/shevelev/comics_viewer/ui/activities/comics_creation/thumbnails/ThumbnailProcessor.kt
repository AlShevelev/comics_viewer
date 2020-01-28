package com.shevelev.comics_viewer.ui.activities.comics_creation.thumbnails

import android.graphics.drawable.Drawable
import com.shevelev.comics_viewer.ui.activities.comics_creation.drag_lists.IListItemDragCreatorImages
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerBase
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskBase
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskProcessingResultBase

/**
 * Thumbnail calculates here
 */
class ThumbnailProcessor (
    maxQueueLen: Int,
    private val listItemImages: IListItemDragCreatorImages,

    // Callback methods
    private val onTaskProcessedCallback: (Int, ThumbnailListIds, Drawable) -> Unit,
    private val onTaskErrorCallback: (Int, ThumbnailListIds) -> Unit,
    private val onTaskSpilledCallback: (Int, ThumbnailListIds) -> Unit,
    private val onStopProcessingCallback: (Any?) -> Unit) : ProducerConsumerBase(maxQueueLen) {

    /**
     * Calculate image
     */
    @Throws(InterruptedException::class)
    override fun processTask(task: ProducerConsumerTaskBase?): ProducerConsumerTaskProcessingResultBase? {
        return try {
            val thumbnailTask = task as ThumbnailTask
            val image = listItemImages.getPageImage(thumbnailTask.fullPathToImageFile)
            ThumbnailTaskResult(thumbnailTask.id, thumbnailTask.listIds, image!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    override fun onStopProcessing(data: Any?) {
        onStopProcessingCallback(data)
    }

    override fun onTaskProcessed(result: ProducerConsumerTaskProcessingResultBase?) {
        val taskResult = result as ThumbnailTaskResult
        onTaskProcessedCallback(taskResult.id, taskResult.listIds, taskResult.pageImage)
    }

    override fun onTaskError(task: ProducerConsumerTaskBase?) {
        val taskResult = task as ThumbnailTask
        onTaskErrorCallback(taskResult.id, taskResult.listIds)
    }

    override fun onTaskSpilled(task: ProducerConsumerTaskBase?) {
        val taskResult = task as ThumbnailTask
        onTaskSpilledCallback(taskResult.id, taskResult.listIds)
    }
}