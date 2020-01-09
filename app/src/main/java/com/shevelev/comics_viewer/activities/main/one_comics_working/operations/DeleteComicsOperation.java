package com.shevelev.comics_viewer.activities.main.one_comics_working.operations;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity;
import com.shevelev.comics_viewer.comics_workers.ComicsDeletor;
import com.shevelev.comics_viewer.common.dialogs.MessageBoxHelper;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.common.rhea.RheaFacade;

/**
 * Operation for delete comics
 */
public class DeleteComicsOperation extends ComicsOperationBase
{
    public DeleteComicsOperation(IOneComicsActivity activity)
    {
        super(activity);
    }

    public void start(long comicsId)
    {
        MessageBoxHelper.createYesNoDialog(context, context.getString(R.string.message_box_delete_query_title), context.getString(R.string.message_box_delete_query),
            () ->
            {
                uiMethods.setUserActionsLock(true);
                uiMethods.setProgressState(true);
                RheaFacade.run(context, new ComicsDeletor(ComicsDeletor.tag, comicsId));

            }, null).show();
    }

    public void complete()
    {
        uiMethods.setProgressState(false);             // this execute after comics creation
        uiMethods.updateBooksList(null);
        uiMethods.setUserActionsLock(false);
    }

    public void completeWithError()
    {
        ToastsHelper.Show(R.string.message_cant_delete_comics_title, ToastsHelper.Position.Center);
    }
}
