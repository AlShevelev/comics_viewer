package com.shevelev.comics_viewer.activities.main.one_comics_working.operations;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity;
import com.shevelev.comics_viewer.common.dialogs.ComicsNameDialog;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;

/**
 * Operation for edit comics
 */
public class EditComicsOperation extends ComicsOperationBase
{
    public EditComicsOperation(IOneComicsActivity activity)
    {
        super(activity);
    }

    public void start(long comicsId)
    {
        Comics comics = DalFacade.Comics.getComicsById(comicsId);
        ComicsNameDialog dialog=new ComicsNameDialog(
                context,
                (result)->{             // We get name here
                    boolean updateResult=DalFacade.Comics.updateNameAndHidden(comicsId, result.title, result.isPrivate);
                    if(updateResult)
                        uiMethods.updateBooksList(null);
                    else
                        ToastsHelper.Show(R.string.message_cant_update_comics_title, ToastsHelper.Position.Center);
                },
                ()->{
                    // That's it - final
                }, R.string.dialog_comics_name_title, R.layout.dialog_enter_name, new ComicsNameDialog.Model(comics.name, comics.isPrivate));
        dialog.show();
    }
}
