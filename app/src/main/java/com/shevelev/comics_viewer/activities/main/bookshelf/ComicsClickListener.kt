package com.shevelev.comics_viewer.activities.main.bookshelf

import android.view.View

class ComicsClickListener(private val dbComicsId: Long, private val onComicsChoosen: (Long) -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        onComicsChoosen(dbComicsId)
    }
}