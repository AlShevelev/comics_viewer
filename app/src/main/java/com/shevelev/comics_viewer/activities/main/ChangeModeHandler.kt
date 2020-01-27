package com.shevelev.comics_viewer.activities.main

import android.content.Context
import android.view.View
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsViewMode

/**
 * Handle change mode of comics view
 * [viewMode] Mode changed callback
 */
class ChangeModeHandler(
    context: Context,
    var viewMode: ComicsViewMode,
    private val modeChanged: (ComicsViewMode) -> Unit) : IChangeModeHandlerView {

    private val activeColor: Int
    private val inactiveColor: Int

    private fun showMode(allComicsControl: TextView, recentComicsControl: TextView) {
        allComicsControl.setTextColor(if (viewMode === ComicsViewMode.ALL) activeColor else inactiveColor)
        recentComicsControl.setTextColor(if (viewMode === ComicsViewMode.ALL) inactiveColor else activeColor)
    }

    override fun onAllComicsClicked(allComicsControl: View, recentComicsControl: View) {
        viewMode = ComicsViewMode.ALL
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
        modeChanged(viewMode)
    }

    override fun onRecentComicsClicked(allComicsControl: View, recentComicsControl: View) {
        viewMode = ComicsViewMode.RECENT
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
        modeChanged(viewMode)
    }

    override fun initState(allComicsControl: View, recentComicsControl: View) {
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
    }

    init {
        activeColor = context.resources.getColor(R.color.bookcase_header_label_active)
        inactiveColor = context.resources.getColor(R.color.bookcase_header_label_inactive)
    }
}