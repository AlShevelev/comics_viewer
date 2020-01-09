package com.shevelev.comics_viewer.activities.main.comics_filters;

import com.shevelev.comics_viewer.activities.main.ComicsSortInfo;
import com.shevelev.comics_viewer.options.OptionsFacade;
import com.shevelev.comics_viewer.options.OptionsKeys;

public class ComicsFilterFactory
{
    public static IComicsFilter getFilter(ComicsViewMode mode)
    {
        boolean isPrivateComicsHidden = OptionsFacade.ShortLivings.get(OptionsKeys.PasswordEntered)==null;

        if(mode== ComicsViewMode.All)
            return new AllComicsFilter(new ComicsSortInfo((c1, c2) -> c1.name.compareTo(c2.name), false), isPrivateComicsHidden);
        else
            return new RecentComicsFilter(null, isPrivateComicsHidden);
    }
}
