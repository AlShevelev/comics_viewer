package com.shevelev.comics_viewer.activities.main.comics_filters

import com.shevelev.comics_viewer.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Comics

/**
 * Get all comics - public and private
 */
class AllComicsFilter(comicsSortInfo: ComicsSortInfo?, isPrivateComicsHidden: Boolean) : FilterBase(comicsSortInfo, isPrivateComicsHidden) {
    override val comicsList: List<Comics>
        get() = DalFacade.Comics.getComics(true)!!
}