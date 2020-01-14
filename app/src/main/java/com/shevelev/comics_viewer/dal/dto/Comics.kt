package com.shevelev.comics_viewer.dal.dto

import com.shevelev.comics_viewer.dal.entities.DbComics
import java.util.*

/**
 * One comics
 */
class Comics {
    var id: Long = 0
    var name: String = ""
    var coverFilename: String? = null
    var creationDate: Date? = null

    /** Last date/time when comics was viewed   */
    var lastViewDate: Date? = null

    /** Index of last viewed page  */
    var lastViewedPageIndex = 0

    /** Total pages in comics  */
    var totalPages = 0

    /** true if comics if hidden - it apears only after pasword was enter  */
    var isPrivate = false

    /** List of pages  */
    var pages: List<Page> = listOf()

    constructor() {}

    constructor(dbComics: DbComics) {
        id = dbComics.id
        coverFilename = dbComics.coverFilename
        creationDate = dbComics.creationDate
        name = dbComics.name ?: ""
        lastViewDate = dbComics.lastViewDate
        lastViewedPageIndex = dbComics.lastViewedPageIndex
        totalPages = dbComics.totalPages
        isPrivate = dbComics.isHidden
    }
}