package com.shevelev.comics_viewer.activities.main.one_comics_working.operations;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity;
import com.shevelev.comics_viewer.activities.view_comics.CurlActivity;
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog;
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.dal.DalFacade;
import com.shevelev.comics_viewer.dal.dto.Comics;
import com.shevelev.comics_viewer.dal.dto.Option;
import com.shevelev.comics_viewer.options.OptionsFacade;
import com.shevelev.comics_viewer.options.OptionsKeys;
import com.shevelev.comics_viewer.options.OptionsValues;

import java.util.Date;

/**
 * Operation for view comics
 */
public class ViewComicsOperation extends ComicsOperationBase
{
    private enum ComicsOpenConditions
    {
        CanOpen,
        ShowCreatePasswordDialog,
        ShowEnterPasswordDialog
    }

    public ViewComicsOperation(IOneComicsActivity activity)
    {
        super(activity);
    }

    /**  */
    public void start(long comicsId)
    {
        ComicsOpenConditions condition = getOpenComicsConditions(comicsId);
        switch (condition)
        {
            case CanOpen: startView(comicsId); break;
            case ShowCreatePasswordDialog: createPasswordAndView(comicsId); break;
            case ShowEnterPasswordDialog: enterPasswordAndView(comicsId); break;
        }
    }

    /**  */
    public void complete(long comicsId)
    {
        uiMethods.updateBooksList(comicsId);
    }

    /** Can we open comics? */
    private ComicsOpenConditions getOpenComicsConditions(long comicsId)
    {
        Comics comics = DalFacade.Comics.getComicsById(comicsId);

        if(!comics.isPrivate)
            return ComicsOpenConditions.CanOpen;         // Public comics can be opened without conditions

        if(OptionsFacade.LongLivings.get(OptionsKeys.Password)!=null)
        {
            if(OptionsFacade.ShortLivings.get(OptionsKeys.PasswordEntered)!=null)
                return ComicsOpenConditions.CanOpen;
            else
                return ComicsOpenConditions.ShowEnterPasswordDialog;
        }
        else
            return ComicsOpenConditions.ShowCreatePasswordDialog;         // No password - need create
    }

    /**  */
    private void startView(long comicsId)
    {
        if(uiMethods.isUserActionsLock())
            return;

        DalFacade.Comics.updateLastViewDate(comicsId, new Date());            // UTC

        CurlActivity.start(context, comicsId);           // Start view comics
    }

    /**  */
    private void createPasswordAndView(long comicsId)
    {
        ToastsHelper.Show(R.string.private_comics_create_password_message, ToastsHelper.Position.Bottom);

        CreatePasswordDialog dialog=new CreatePasswordDialog(
            context,
            (result)->
            {
                OptionsFacade.LongLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.Password, result.password), new Option(OptionsKeys.PasswordsHint, result.hint)});
                OptionsFacade.ShortLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.PasswordEntered, OptionsValues.True)});

                startView(comicsId);
            },
            ()-> { });

        dialog.show();
    }

    /**  */
    private void enterPasswordAndView(long comicsId)
    {
        String password = OptionsFacade.LongLivings.get(OptionsKeys.Password);
        String hint = OptionsFacade.LongLivings.get(OptionsKeys.PasswordsHint);

        EnterPasswordDialog dialog=new EnterPasswordDialog(
                context,
                (result)->
                {
                    if(result.equals(password))
                    {
                        OptionsFacade.ShortLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.PasswordEntered, OptionsValues.True)});
                        startView(comicsId);
                    }
                    else
                        ToastsHelper.Show(R.string.message_invalid_password, ToastsHelper.Position.Center);
                },
                ()->
                { }, hint);

        dialog.show();
    }
}
