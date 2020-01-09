package com.shevelev.comics_viewer.activities.main.one_comics_working;

import com.shevelev.comics_viewer.activities.main.one_comics_working.operations.CreateComicsOperation;
import com.shevelev.comics_viewer.activities.main.one_comics_working.operations.DeleteComicsOperation;
import com.shevelev.comics_viewer.activities.main.one_comics_working.operations.EditComicsOperation;
import com.shevelev.comics_viewer.activities.main.one_comics_working.operations.ViewComicsOperation;

/**
 * Facade for working with one concrete comics
 */
public class ComicsWorkingFacade
{
    public EditComicsOperation edit;
    public DeleteComicsOperation delete;
    public ViewComicsOperation view;
    public CreateComicsOperation create;

    public ComicsWorkingFacade(IOneComicsActivity activity)
    {
        edit = new EditComicsOperation(activity);
        delete = new DeleteComicsOperation(activity);
        view = new ViewComicsOperation(activity);
        create = new CreateComicsOperation(activity);
    }
}
