package com.shevelev.comics_viewer.ui.activities.main.comics_filters

import java.util.*

/**
 * Dto for one comics for bookcase
 */
class BookcaseComics {
    /** Id in Db of comics   */
    var id: Long = 0
    /** Name of comics  */
    var name: String? = null
    /** Name to display (may be not equals with @name for private comics)  */
    var displayName: String? = null
    /** File name of cover without path  */
    var coverFilename: String? = null
    /** If true we should show private's comics cover (stub)  */
    var isNeedShowPrivateCover = false
    /** Creation moment  */
    var creationDate: Date? = null
    /** Last date/time when comics was viewed   */
    var lastViewDate: Date? = null
    /** Index of last viewed page  */
    var lastViewedPageIndex = 0
    /** Total pages in comics  */
    var totalPages = 0
    /** true if comics is private - it apears only after pasword was enter  */
    var isPrivate = false

    constructor() {}

    constructor(
        id: Long,
        name: String?,
        displayName: String?,
        coverFilename: String?,
        isNeedShowPrivateCover: Boolean,
        creationDate: Date?,
        lastViewDate: Date?,
        lastViewedPageIndex: Int,
        totalPages: Int,
        isPrivate: Boolean) {
        this.id = id
        this.name = name
        this.displayName = displayName
        this.coverFilename = coverFilename
        this.isNeedShowPrivateCover = isNeedShowPrivateCover
        this.creationDate = creationDate
        this.lastViewDate = lastViewDate
        this.lastViewedPageIndex = lastViewedPageIndex
        this.totalPages = totalPages
        this.isPrivate = isPrivate
    }
}