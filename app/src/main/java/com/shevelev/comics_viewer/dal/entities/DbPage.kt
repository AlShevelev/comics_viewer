package com.shevelev.comics_viewer.dal.entities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.shevelev.comics_viewer.dal.dto.Page

/**
 * One page of comics
 */
@Table(name = "Page")
class DbPage : Model {
    @Column(name = "FileName")
    var fileName: String? = null

    @Column(name = "SortingOrder")
    var order = 0

    @Column(name = "Comics")
    var comics: DbComics? = null

    @Column(name = "IsLeftTopCornerDark")
    var isLeftTopCornerDark = false

    @Column(name = "IsLeftBottomCornerDark")
    var isLeftBottomCornerDark = false

    @Column(name = "IsRightTopCornerDark")
    var isRightTopCornerDark = false

    @Column(name = "IsRightBottomCornerDark")
    var isRightBottomCornerDark = false

    constructor() {}
    constructor(page: Page, comics: DbComics?) {
        this.comics = comics
        fileName = page.fileName
        order = page.order
        isLeftBottomCornerDark = page.isLeftBottomCornerDark
        isLeftTopCornerDark = page.isLeftTopCornerDark
        isRightBottomCornerDark = page.isRightBottomCornerDark
        isRightTopCornerDark = page.isRightTopCornerDark
    }
}