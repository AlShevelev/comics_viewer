package com.shevelev.comics_viewer.dal.dto

import com.shevelev.comics_viewer.comics_workers.PreviewCreator.Companion.getPreviewFileName
import com.shevelev.comics_viewer.dal.entities.DbPage

/**
 * One page of comics
 */
class Page {
    var id: Long = 0
    /** Filename without path  */
    var fileName: String? = null
    var order = 0
    var isLeftTopCornerDark = false
    var isLeftBottomCornerDark = false
    var isRightTopCornerDark = false
    var isRightBottomCornerDark = false

    constructor() {}
    constructor(dbPage: DbPage) {
        fileName = dbPage.fileName
        order = dbPage.order
        id = dbPage.id
        isLeftBottomCornerDark = dbPage.isLeftBottomCornerDark
        isLeftTopCornerDark = dbPage.isLeftTopCornerDark
        isRightBottomCornerDark = dbPage.isRightBottomCornerDark
        isRightTopCornerDark = dbPage.isRightTopCornerDark
    }

    val previewFileName: String?
        get() = getPreviewFileName(fileName)
}