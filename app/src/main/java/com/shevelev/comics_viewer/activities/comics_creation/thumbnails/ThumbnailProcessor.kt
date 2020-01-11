package com.shevelev.comics_viewer.activities.comics_creation.thumbnails

import android.graphics.drawable.Drawable
import com.shevelev.comics_viewer.activities.comics_creation.drag_lists.IListItemDragCreatorImages
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionThreeArgs
import com.shevelev.comics_viewer.common.func_interfaces.IActionTwoArgs
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
    private val onTaskProcessedCallback: IActionThreeArgs<Int, ThumbnailListIds, Drawable>,
    private val onTaskErrorCallback: IActionTwoArgs<Int, ThumbnailListIds>,
    private val onTaskSpilledCallback: IActionTwoArgs<Int, ThumbnailListIds>,
    private val onStopProcessingCallback: IActionOneArgs<Any>) : ProducerConsumerBase(maxQueueLen) {

    /**
     * Calculate image
     */
    @Throws(InterruptedException::class)
    override fun processTask(task: ProducerConsumerTaskBase): ProducerConsumerTaskProcessingResultBase? {
        return try {
            val thumbnailTask = task as ThumbnailTask
            val image = listItemImages.getPageImage(thumbnailTask.fullPathToImageFile)
            ThumbnailTaskResult(thumbnailTask.id, thumbnailTask.listIds, image!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    override fun onStopProcessing(data: Any) {
        onStopProcessingCallback.process(data)
    }

    override fun onTaskProcessed(result: ProducerConsumerTaskProcessingResultBase) {
        val taskResult = result as ThumbnailTaskResult
        onTaskProcessedCallback.process(taskResult.id, taskResult.listIds, taskResult.pageImage)
    }

    override fun onTaskError(task: ProducerConsumerTaskBase) {
        val taskResult = task as ThumbnailTask
        onTaskErrorCallback.process(taskResult.id, taskResult.listIds)
    }

    override fun onTaskSpilled(task: ProducerConsumerTaskBase) {
        val taskResult = task as ThumbnailTask
        onTaskSpilledCallback.process(taskResult.id, taskResult.listIds)
    }
}