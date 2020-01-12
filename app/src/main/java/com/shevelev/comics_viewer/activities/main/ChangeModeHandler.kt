package com.shevelev.comics_viewer.activities.main

import android.view.View
import android.widget.TextView
import com.shevelev.comics_viewer.App.Main.context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsViewMode
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs

/**
 * Handle change mode of comics view
 */
class ChangeModeHandler(var viewMode: ComicsViewMode, // Mode changed callback
                        private val modeChanged: IActionOneArgs<ComicsViewMode>) : IChangeModeHandlerView {
    private val activeColor: Int
    private val inactiveColor: Int

    private fun showMode(allComicsControl: TextView, recentComicsControl: TextView) {
        allComicsControl.setTextColor(if (viewMode === ComicsViewMode.ALL) activeColor else inactiveColor)
        recentComicsControl.setTextColor(if (viewMode === ComicsViewMode.ALL) inactiveColor else activeColor)
    }

    override fun onAllComicsClicked(allComicsControl: View, recentComicsControl: View) {
        viewMode = ComicsViewMode.ALL
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
        modeChanged.process(viewMode)
    }

    override fun onRecentComicsClicked(allComicsControl: View, recentComicsControl: View) {
        viewMode = ComicsViewMode.RECENT
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
        modeChanged.process(viewMode)
    }

    override fun initState(allComicsControl: View, recentComicsControl: View) {
        showMode(allComicsControl as TextView, recentComicsControl as TextView)
    }

    init {
        val resources = context!!.resources
        activeColor = resources.getColor(R.color.bookcase_header_label_active)
        inactiveColor = resources.getColor(R.color.bookcase_header_label_inactive)
    }
}