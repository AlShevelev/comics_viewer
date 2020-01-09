package com.shevelev.comics_viewer.activities.comics_creation;

import android.app.Activity;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.App;
import com.shevelev.comics_viewer.common.func_interfaces.IActionThreeArgs;
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper;
import com.shevelev.comics_viewer.common.dialogs.ComicsNameDialog;
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo;
import com.shevelev.comics_viewer.common.dialogs.MessageBoxHelper;

/**
 * First step of comics creation - checking creation posibility and choosing name
 */
public class ChooseComicsName
{
    private String _pathToFolder;

    private final Activity _context;

    private final IActionThreeArgs<String, Boolean, String> _nameChoosed;

    public ChooseComicsName(Activity context, IActionThreeArgs<String, Boolean, String> nameChoosed)
    {
        _context = context;
        _nameChoosed = nameChoosed;
    }

    /**
     * It's magic - we createFromFile comics here
     */
    public void startCreate(String pathToFolder)
    {
        _pathToFolder = pathToFolder;
        FolderInfo folderInfo=new FolderInfo(_pathToFolder);

        boolean canCreate=!CollectionsHelper.isNullOrEmpty(folderInfo.getImages());
        if(canCreate)
            startGetComicsName(folderInfo.getName());
        else
        {
            MessageBoxHelper.createOkDialog(
                    _context,
                    App.getContext().getString(R.string.message_box_cant_create_comics_title),
                    App.getContext().getString(R.string.message_box_no_imges_text),
                    () -> {
                    }).show();
        }
    }

    private void startGetComicsName(String initName)
    {
        ComicsNameDialog dialog=new ComicsNameDialog(
        _context,
        (result)->{             // We get name here
            if(_nameChoosed !=null)
                _nameChoosed.process(result.title, result.isPrivate, _pathToFolder);
        },
        ()->{
                        // That's it - final
        }, R.string.dialog_comics_name_title, R.layout.dialog_enter_name, new ComicsNameDialog.Model(initName, false));

        dialog.show();
    }
}
