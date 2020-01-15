package com.shevelev.comics_viewer.activities.comics_creation.thumbnails

import android.graphics.drawable.Drawable
import com.shevelev.comics_viewer.activities.comics_creation.drag_lists.IListItemDragCreatorImages
import com.shevelev.comics_viewer.activities.comics_creation.drag_lists.ListItemDrag
import com.shevelev.comics_viewer.common.collections.DynamicList
import com.shevelev.comics_viewer.common.collections.SpillingQueue
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskBase
import com.shevelev.comics_viewer.common.producer_consumer.ProducerConsumerTaskKinds
import kotlin.math.min

/**
 * Core class for thumbnails calculation
 * [stopCallback] Called when processing stopped
 */
class ThumbnailManager(private val listItemImages: IListItemDragCreatorImages, private val stopCallback: (Any?) -> Unit) {
    companion object {
        private const val LIST_IMAGES_MAX_CAPACITY = 25 // Max len of caching queues
        private const val IMAGES_PROCESSOR_MAX_CAPACITY = 25 // Max len of queues for processing images
    }

    private val firstListImages = SpillingQueue<ThumbnailCalculationResult>(LIST_IMAGES_MAX_CAPACITY)
    private val secondListImages = SpillingQueue<ThumbnailCalculationResult>(LIST_IMAGES_MAX_CAPACITY)

    private val imagesProcessor = ThumbnailProcessor(
        IMAGES_PROCESSOR_MAX_CAPACITY,
        listItemImages,
        { id: Int, listId: ThumbnailListIds, pageImage: Drawable -> onImageCalculated(id, listId, pageImage) },
        { id: Int, listId: ThumbnailListIds -> onImageCalculationError(id, listId) },
        { id: Int, listId: ThumbnailListIds -> onImageCalculationSpilled(id, listId) },
        { dataToReturn: Any? -> onStopProcessing(dataToReturn) })

    private val waitingControls = DynamicList<IThumbnailControl>()   // Controls waiting for images

    private val warmUpCacheDeep = 5 // How many items will be placed in cache while warming up

    /**
     * Warm up caches - put some images into its
     */
    fun warmUpCaches(listItems: List<ListItemDrag>) {
        val itemToPlace = min(warmUpCacheDeep, listItems.size)

        for (i in 0 until itemToPlace) {
            val item = listItems[i]
            val result = imagesProcessor.processSync(ThumbnailTask(item.id, ThumbnailListIds.LEFT, item.fullPathToImageFile))
                ?: break
            val typedResult = result as ThumbnailTaskResult
            firstListImages.push(ThumbnailCalculationResult(typedResult.id, typedResult.pageImage))
            secondListImages.push(ThumbnailCalculationResult(typedResult.id, typedResult.pageImage))
        }
    }

    /**
     * Get cached images for pages list
     */
    private fun getListImages(listId: ThumbnailListIds?): SpillingQueue<ThumbnailCalculationResult> =
        if (listId === ThumbnailListIds.LEFT) firstListImages else secondListImages

    /**
     * Get cached images for another list
     */
    private fun getAnotherListImages(listId: ThumbnailListIds?): SpillingQueue<ThumbnailCalculationResult> =
        if (listId === ThumbnailListIds.LEFT) secondListImages else firstListImages

    /**
     * Get thumbnail for control
     * @param thumbnailControl control needed thumbnail
     */
    fun getThumbnail(thumbnailControl: IThumbnailControl) {
        // Control already in waiting list
        if (waitingControls.isExists( { t: IThumbnailControl -> t.id == thumbnailControl.id && t.listId === thumbnailControl.listId }))
            return

        val oldControl = waitingControls.extract( { t: IThumbnailControl -> t.imageHashCode == thumbnailControl.imageHashCode })
        if (oldControl != null) { // ListView reuses controls, so we must remove old control and its taks
            imagesProcessor.removeTask( { t: ProducerConsumerTaskBase ->
                if (t.kind != ProducerConsumerTaskKinds.Normal) {
                    false
                } else {
                    val typedTask = t as ThumbnailTask
                    typedTask.id == thumbnailControl.id && typedTask.listIds === thumbnailControl.listId
                }
            })
        }
        val imagesList = getListImages(thumbnailControl.listId) // Try to find image in our list
        var cachedImage = imagesList.getAndMoveToHead( { t: ThumbnailCalculationResult -> t.id == thumbnailControl.id })

        if (cachedImage != null) { // return cached image
            thumbnailControl.setThumbnail(cachedImage.pageImage)
            return
        }

        val anotherListImages = getAnotherListImages(thumbnailControl.listId) // Second attempt - in another list
        cachedImage = anotherListImages[ { t: ThumbnailCalculationResult -> t.id == thumbnailControl.id }]
        if (cachedImage != null) {
            imagesList.push(cachedImage) // push in our list
            thumbnailControl.setThumbnail(cachedImage.pageImage) // and return cached image
            return
        }
        // image not found - we need to calculate it
        thumbnailControl.setThumbnail(listItemImages.stubImage) // set stub image
        waitingControls.push(thumbnailControl) // memorize control...
        imagesProcessor.processAsync(ThumbnailTask(thumbnailControl.id, thumbnailControl.listId!!, thumbnailControl.fullSourceFileName!!)) // and start calculation
    }

    /**
     * Image calculated successfully
     * @param id id of control
     * @param pageImage calculated image
     */
    private fun onImageCalculated(id: Int, listId: ThumbnailListIds, pageImage: Drawable) {
        val control = waitingControls.extract( { item: IThumbnailControl -> item.id == id && item.listId === listId })
            ?: return // remove control from waiting list
        val imagesList = getListImages(control.listId) // push image to cache
        imagesList.push(ThumbnailCalculationResult(id, pageImage))
        control.setThumbnail(pageImage) // and display it
    }

    /**
     * There was an error while calculate image
     * @param id id of control
     */
    private fun onImageCalculationError(id: Int, listId: ThumbnailListIds) =
        waitingControls.extract( { item: IThumbnailControl -> item.id == id && item.listId === listId }) // remove control from waiting list

    /**
     * Calculation was spilled
     * @param id id of control
     */
    private fun onImageCalculationSpilled(id: Int, listId: ThumbnailListIds) =
        waitingControls.extract( { item: IThumbnailControl -> item.id == id && item.listId === listId }) // remove control from waiting list

    /**
     * Start processing images
     */
    fun start() = imagesProcessor.start()

    /**
     * Command to stop processing
     * @param dataToReturn some data (state) need to return when processing'll stop
     */
    fun stop(dataToReturn: Any?) = imagesProcessor.stop(dataToReturn)

    private fun onStopProcessing(dataToReturn: Any?) = stopCallback(dataToReturn)
}