package com.shevelev.comics_viewer.comics_workers

import android.graphics.Bitmap
import android.util.Log
import com.shevelev.comics_viewer.common.helpers.BitmapDarkRate
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.common.helpers.files.BitmapsQuality
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.rhea.RheaOperationBase
import com.shevelev.comics_viewer.common.structs.AreaF
import com.shevelev.comics_viewer.common.structs.PointF
import com.shevelev.comics_viewer.common.structs.Size
import com.shevelev.comics_viewer.common.structs.SizeRelative
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Comics
import com.shevelev.comics_viewer.dal.dto.Page
import java.util.*

/**
 * Comics is created here (result [Long]: id of comics in db (or null if something happend))
 */
class ComicsCreator(
    tag: String?,
    // Name of comics
    private val name: String,
    // Comics is private
    private val isPrivate: Boolean,
    // Source images of pages
    private val sourceImages: List<DiskItemInfo>,

    clientSize: Size) : RheaOperationBase(tag) {

    private lateinit var diskItemsSortedIds : IntArray       // Sorted disk items
    private val previewCreator: IPreviewCreator = PreviewCreator(clientSize)
    /**
     * Set disk items before comics creation
     * @param diskItemsSortedIds - sorted disk items
     */
    fun setDiskItems(diskItemsSortedIds: IntArray) {
        this.diskItemsSortedIds = diskItemsSortedIds
    }

    private fun createFileName(): String {
        return UUID.randomUUID().toString()
    }

    private fun sourceImagesToDict(): Map<Int, DiskItemInfo> {
        val result: MutableMap<Int, DiskItemInfo> = TreeMap() // Key: DiskItemInfo::Id, Value: name of file
        for (sourceImage in sourceImages) result[sourceImage.id] = sourceImage
        return result
    }

    /**
     * Create preview and coping files of pages into private area
     * @return dictionary with ids and files names if success
     */
    private fun createPagesAndCopyImages(pageFilesByIds: Map<Int, DiskItemInfo>, diskItemsSortedIds: IntArray): ArrayList<Page>? {
        val total = diskItemsSortedIds.size
        val result = ArrayList<Page>(total)
        for (i in 0 until total) {
            val sourceImage = pageFilesByIds[diskItemsSortedIds[i]]
            val sourceFullName = sourceImage!!.fullname
            val pageFileName = createFileName()
            val previewFileName = PreviewCreator.getPreviewFileName(pageFileName)
            val previewBitmap = previewCreator.createPreviewAndSave(sourceFullName, previewFileName)
                ?: return null // Create and save preview
            if (!AppPrivateFilesHelper.createFromFile(sourceFullName, pageFileName)) // Copy images into private area
                return null
            val page = Page()
            page.fileName = pageFileName
            page.order = i
            calculateCornerDarkness(previewBitmap, page) // Calculate darrness of corners
            result.add(page)
            updateProgress(i + 1, total)
        }
        return result
    }

    private fun calculateCornerDarkness(source: Bitmap, page: Page) {
        var area = AreaF(PointF(0.85f, 0.85f), SizeRelative(0.15f, 0.15f)) // right and bottom
        page.isRightBottomCornerDark = BitmapsHelper.isDark(source, area) == BitmapDarkRate.Dark
        area = AreaF(PointF(0f, 0.85f), SizeRelative(0.15f, 0.15f))
        page.isLeftBottomCornerDark = BitmapsHelper.isDark(source, area) == BitmapDarkRate.Dark
        area = AreaF(PointF(0f, 0f), SizeRelative(0.15f, 0.15f))
        page.isLeftTopCornerDark = BitmapsHelper.isDark(source, area) == BitmapDarkRate.Dark
        area = AreaF(PointF(0.85f, 0f), SizeRelative(0.15f, 0.15f))
        page.isRightTopCornerDark = BitmapsHelper.isDark(source, area) == BitmapDarkRate.Dark
    }

    /**
     *
     * @param diskItemsSortedIds
     * @return file name with cover (in private area without path)
     */
    private fun createCover(diskItemsSortedIds: IntArray, pageFilesByIds: Map<Int, DiskItemInfo>): String? {
        return try {
            val firstId = diskItemsSortedIds[0] // Cover is a first page
            val sourceCover = pageFilesByIds[firstId]
            val coverBitmapScaled = CoverCreator.create(sourceCover!!.fullname, previewCreator) // Create cover
            val destinationFileName = createFileName() // Create file name for it
            AppPrivateFilesHelper.createFromBitmap(
                destinationFileName,
                coverBitmapScaled,
                BitmapsQuality.Low,
                Bitmap.CompressFormat.PNG) // Save to disk
            destinationFileName // and return file name
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        }
    }

    private fun saveToDb(coverFileName: String, pages: ArrayList<Page>): Long {
        val comics = Comics()
        comics.name = name
        comics.isPrivate = isPrivate
        comics.coverFilename = coverFileName
        comics.creationDate = Date() // UTC
        comics.totalPages = pages.size
        return DalFacade.Comics.createComics(comics, pages)!!
    }

    override fun process(): Any? {
        val sourceImageDict = sourceImagesToDict()
        val pages = createPagesAndCopyImages(sourceImageDict, diskItemsSortedIds) ?: return null
        val coverFilename = createCover(diskItemsSortedIds, sourceImageDict)
        return coverFilename?.let { saveToDb(it, pages) }
    }

    companion object {
        const val tag = "ComicsCreationRhea"
    }
}