package com.shevelev.comics_viewer.activities.comics_creation.thumbnails;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.shevelev.comics_viewer.activities.comics_creation.drag_lists.IListItemDragCreatorImages;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.func_interfaces.IActionThreeArgs;
import com.shevelev.comics_viewer.common.func_interfaces.IActionTwoArgs;
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerBase;
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskBase;
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskProcessingResultBase;

/**
 * Thumbnail calculates here
 */
public class ThumbnailProcessor extends ProducerConsumerBase
{
    private final IListItemDragCreatorImages listItemImages;

    private IActionThreeArgs<Integer, ThumbnailListIds, Drawable> onTaskProcessedCallback;            // Callback methods
    private IActionTwoArgs<Integer, ThumbnailListIds> onTaskErrorCallback;
    private IActionTwoArgs<Integer, ThumbnailListIds> onTaskSpilledCallback;
    private IActionOneArgs<Object> onStopProcessingCallback;

    public ThumbnailProcessor(
            int maxQueueLen,
            IListItemDragCreatorImages listItemImages,
            IActionThreeArgs<Integer, ThumbnailListIds, Drawable> onTaskProcessedCallback,
            IActionTwoArgs<Integer, ThumbnailListIds> onTaskErrorCallback,
            IActionTwoArgs<Integer, ThumbnailListIds> onTaskSpilledCallback,
            IActionOneArgs<Object> onStopProcessingCallback)
    {
        super(maxQueueLen);
        this.listItemImages = listItemImages;

        this.onTaskProcessedCallback = onTaskProcessedCallback;
        this.onTaskErrorCallback = onTaskErrorCallback;
        this.onTaskSpilledCallback = onTaskSpilledCallback;
        this.onStopProcessingCallback = onStopProcessingCallback;
    }

    /**
     * Calculate image
     */
    @Override
    protected ProducerConsumerTaskProcessingResultBase processTask(ProducerConsumerTaskBase task) throws InterruptedException
    {
        try
        {
            ThumbnailTask thumbnailTask = (ThumbnailTask)task;

            Drawable image=listItemImages.getPageImage(thumbnailTask.getFullPathToImageFile());

            return new ThumbnailTaskResult(thumbnailTask.getId(), thumbnailTask.getListIds(), image);
        }
        catch(Exception ex)
        {
            Log.e("ThumbnailProcessor", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    protected void onStopProcessing(Object data)
    {
        onStopProcessingCallback.process(data);
    }

    @Override
    protected void onTaskProcessed(ProducerConsumerTaskProcessingResultBase result)
    {
        ThumbnailTaskResult taskResult = (ThumbnailTaskResult)result;
        onTaskProcessedCallback.process(taskResult.getId(), taskResult.getListIds(), taskResult.getPageImage());
    }

    @Override
    protected void onTaskError(ProducerConsumerTaskBase task)
    {
        ThumbnailTask taskResult = (ThumbnailTask)task;
        onTaskErrorCallback.process(taskResult.getId(), taskResult.getListIds());
    }

    @Override
    protected void onTaskSpilled(ProducerConsumerTaskBase task)
    {
        ThumbnailTask taskResult = (ThumbnailTask)task;
        onTaskSpilledCallback.process(taskResult.getId(), taskResult.getListIds());
    }
}