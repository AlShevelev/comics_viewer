package com.shevelev.comics_viewer.comics_workers;

import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper;
import com.shevelev.comics_viewer.common.rhea.RheaOperationBase;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;
import com.shevelev.comics_viewer.dal.dto.Page;

/**
 * Comics is deleted here
 */
public class ComicsDeletor extends RheaOperationBase
{
    private final Long comicsId;

    public static final String tag = "ComicsDeletionRhea";

    public ComicsDeletor(String tag, long comicsId)
    {
        super(tag);
        this.comicsId = comicsId;
    }


    @Override
    protected Object process()
    {
        Comics comics=DalFacade.Comics.deleteComics(this.comicsId);
        if(comics!=null)
            if(AppPrivateFilesHelper.delete(comics.coverFilename))
                for (Page page : comics.pages)
                    AppPrivateFilesHelper.delete(page.fileName);
        return null;
    }
}
