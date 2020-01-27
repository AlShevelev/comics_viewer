package com.shevelev.comics_viewer.activities.main.comics_filters

import android.content.Context
import com.shevelev.comics_viewer.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Comics

/**
 * Get all comics - public and private
 */
class AllComicsFilter(
    context: Context,
    comicsSortInfo: ComicsSortInfo?,
    isPrivateComicsHidden: Boolean
) : FilterBase(context, comicsSortInfo, isPrivateComicsHidden) {
    override val comicsList: List<Comics>
        get() = DalFacade.Comics.getComics(true)!!
}