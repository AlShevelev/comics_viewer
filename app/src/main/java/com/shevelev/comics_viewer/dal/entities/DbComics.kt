package com.shevelev.comics_viewer.dal.entities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.shevelev.comics_viewer.dal.dto.Comics
import java.util.*

/**
 * One comics
 */
@Table(name = "Comics")
class DbComics : Model {
    @Column(name = "Name")
    var name: String? = null

    @Column(name = "CoverFilename")
    var coverFilename: String? = null

    @Column(name = "CreationDate")
    var creationDate: Date? = null

    /** Last date/time when comics was viewed  */
    @Column(name = "LastViewDate")
    var lastViewDate: Date? = null

    /** Index of last viewed page  */
    @Column(name = "LastViewedPageIndex")
    var lastViewedPageIndex = 0

    /** Total pages in comics  */
    @Column(name = "TotalPages")
    var totalPages = 0

    /** true if comics if hidden - it apears only after pasword was enter  */
    @Column(name = "IsHidden")
    var isHidden = false

    constructor() {}

    constructor(comics: Comics) {
        name = comics.name
        coverFilename = comics.coverFilename
        creationDate = comics.creationDate
        lastViewDate = comics.lastViewDate
        lastViewedPageIndex = comics.lastViewedPageIndex
        totalPages = comics.totalPages
        isHidden = comics.isPrivate
    }

    /**
     * Get all pages of comics
     */
    val pages: List<DbPage>
        get() = getMany(DbPage::class.java, "Comics")
}