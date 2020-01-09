package com.shevelev.comics_viewer.activities.main;

import android.view.View;

public interface IChangeModeHandlerView
{
    void onAllComicsClicked(View allComicsControl, View recentComicsControl);
    void onRecentComicsClicked(View allComicsControl, View recentComicsControl);

    void initState(View allComicsControl, View recentComicsControl);
}
