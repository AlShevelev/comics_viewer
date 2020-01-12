package com.shevelev.comics_viewer.activities.main.bookshelf

import android.graphics.Bitmap

/**
 * One comics info for BookshelfListAdapter
 */
class BookshelfComicsInfo(
    val id: Long,           // id from Db
    val title: String,
    val image: Bitmap,
    val isPrivateAndClosed: Boolean     // is comics private and password was not entered
)