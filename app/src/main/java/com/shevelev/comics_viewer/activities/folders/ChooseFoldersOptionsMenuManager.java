package com.shevelev.comics_viewer.activities.folders;

import android.view.Menu;

import com.shevelev.comics_viewer.common.OptionsMenuManager;

public class ChooseFoldersOptionsMenuManager extends OptionsMenuManager
{
    public ChooseFoldersOptionsMenuManager(Menu menu)
    {
        super(menu);
    }

    public void setAcceptVisible(boolean visible)
    {
        setVisible(1, visible);
    }
}
