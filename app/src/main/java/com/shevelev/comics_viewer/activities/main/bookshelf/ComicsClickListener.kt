package com.shevelev.comics_viewer.activities.main.bookshelf

import android.view.View
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs

class ComicsClickListener(private val dbComicsId: Long, private val onComicsChoosen: IActionOneArgs<Long>) : View.OnClickListener {
    override fun onClick(v: View) {
        onComicsChoosen.process(dbComicsId)
    }
}