package com.shevelev.comics_viewer.activities.main.comics_filters;

import com.shevelev.comics_viewer.activities.main.ComicsSortInfo;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;

import java.util.List;

/**
 * Get all comics - public and private
 */
public class AllComicsFilter extends FilterBase
{
    public AllComicsFilter(ComicsSortInfo comicsSortInfo, boolean isPrivateComicsHidden)
    {
        super(comicsSortInfo, isPrivateComicsHidden);
    }

    @Override
    protected List<Comics> getComicsList()
    {
        return DalFacade.Comics.getComics(true);
    }
}
