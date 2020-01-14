package com.shevelev.comics_viewer.activities.main.comics_filters

import com.shevelev.comics_viewer.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Comics

/**
 * Get recent comics - public and private
 */
class RecentComicsFilter(comicsSortInfo: ComicsSortInfo?, isPrivateComicsHidden: Boolean) : FilterBase(comicsSortInfo, isPrivateComicsHidden) {
    override val comicsList: List<Comics>
        protected get() {
            var result = DalFacade.Comics.getComics(true)
            result = CollectionsHelper.where(result) { item: Comics -> item.lastViewDate != null }
            result = CollectionsHelper.sort(result, { c1: Comics, c2: Comics -> c1.lastViewDate?.compareTo(c2.lastViewDate) ?: 0 }, true)
            result = CollectionsHelper.take(result, comicsToReturn, 0)
            return result
        }

    companion object {
        private const val comicsToReturn = 6
    }
}