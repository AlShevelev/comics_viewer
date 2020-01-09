package com.shevelev.comics_viewer.activities.main.comics_filters;


import com.shevelev.comics_viewer.activities.main.ComicsSortInfo;
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;

import java.util.List;

/**
 * Get recent comics - public and private
 */
public class RecentComicsFilter extends FilterBase
{
    private static final int comicsToReturn=6;

    public RecentComicsFilter(ComicsSortInfo comicsSortInfo, boolean isPrivateComicsHidden)
    {
        super(comicsSortInfo, isPrivateComicsHidden);
    }

    @Override
    protected List<Comics> getComicsList()
    {
        List<Comics> result =  DalFacade.Comics.getComics(true);

        result = CollectionsHelper.where(result, item->item.lastViewDate!=null);
        result = CollectionsHelper.sort(result, (c1, c2) -> c1.lastViewDate.compareTo(c2.lastViewDate), true);
        result = CollectionsHelper.take(result, comicsToReturn, 0);

        return result;
    }
}
