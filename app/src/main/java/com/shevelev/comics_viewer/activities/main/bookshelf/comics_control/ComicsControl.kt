package com.shevelev.comics_viewer.activities.main.bookshelf.comics_control

import android.app.Activity
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsInfo
import com.shevelev.comics_viewer.activities.main.bookshelf.ComicsClickListener
import com.shevelev.comics_viewer.common.helpers.StringsHelper

class ComicsControl(
    activity: Activity,
    private val comicsInfo: BookshelfComicsInfo,
    maxComicsWidth: Int,
    onComicsChoosen: (Long) -> Unit
) : LinearLayout(activity) {
    private fun createComicsTitle(sourceTitle: String): String {
        return StringsHelper.cutToLength(sourceTitle, MAX_TEXT_LEN)
    }

    override fun getContextMenuInfo(): ContextMenuInfo {
        val comicsContextMenuInfo = ComicsContextMenuInfo()
        comicsContextMenuInfo.dbId = comicsInfo.id
        comicsContextMenuInfo.menuTitle = comicsInfo.title
        return comicsContextMenuInfo
    }

    companion object {
        private const val MAX_TEXT_LEN = 35
    }

    init {
        View.inflate(activity, R.layout.bookshelf_one_comics, this)
        val coverImage = findViewById<View>(R.id.comicsCoverImage) as ImageView
        val coverText = findViewById<View>(R.id.comicsCoverText) as TextView
        val title = createComicsTitle(comicsInfo.title)
        coverText.text = title
        coverImage.setImageBitmap(comicsInfo.image)
        val pc = ViewGroup.LayoutParams(maxComicsWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams = pc
        setOnClickListener(ComicsClickListener(comicsInfo.id, onComicsChoosen))
        if (comicsInfo.isPrivateAndClosed) // To prevent long click on private&closed comics
            setOnLongClickListener { v: View? -> true }
        activity.registerForContextMenu(this)
    }
}