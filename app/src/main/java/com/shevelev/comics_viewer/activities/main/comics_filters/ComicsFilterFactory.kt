package com.shevelev.comics_viewer.activities.main.comics_filters

import com.shevelev.comics_viewer.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.dal.dto.Comics
import com.shevelev.comics_viewer.options.OptionsFacade
import com.shevelev.comics_viewer.options.OptionsKeys

object ComicsFilterFactory {
    fun getFilter(mode: ComicsViewMode): IComicsFilter {
        val isPrivateComicsHidden = OptionsFacade.ShortLivings[OptionsKeys.PasswordEntered] == null
        return if (mode == ComicsViewMode.ALL) {
            AllComicsFilter(ComicsSortInfo(java.util.Comparator { c1: Comics, c2: Comics -> c1.name.compareTo(c2.name) }, false), isPrivateComicsHidden)
        }
        else {
            RecentComicsFilter(null, isPrivateComicsHidden)
        }
    }
}