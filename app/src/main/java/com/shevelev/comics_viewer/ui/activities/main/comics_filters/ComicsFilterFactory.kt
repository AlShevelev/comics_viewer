package com.shevelev.comics_viewer.ui.activities.main.comics_filters

import android.content.Context
import com.shevelev.comics_viewer.ui.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.dal.dto.Comics

object ComicsFilterFactory {
    fun getFilter(context: Context, keyValueStorage: KeyValueStorageFacade, mode: ComicsViewMode): IComicsFilter {
        val isPrivateComicsHidden = !(keyValueStorage.getPasswordEntered() ?: false)
        return if (mode == ComicsViewMode.ALL) {
            AllComicsFilter(context, ComicsSortInfo(java.util.Comparator { c1: Comics, c2: Comics -> c1.name.compareTo(c2.name) }, false), isPrivateComicsHidden)
        }
        else {
            RecentComicsFilter(context, null, isPrivateComicsHidden)
        }
    }
}