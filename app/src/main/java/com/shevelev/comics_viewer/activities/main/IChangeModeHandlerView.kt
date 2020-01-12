package com.shevelev.comics_viewer.activities.main

import android.view.View

interface IChangeModeHandlerView {
    fun onAllComicsClicked(allComicsControl: View, recentComicsControl: View)
    fun onRecentComicsClicked(allComicsControl: View, recentComicsControl: View)
    fun initState(allComicsControl: View, recentComicsControl: View)
}