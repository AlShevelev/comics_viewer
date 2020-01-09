package com.shevelev.comics_viewer.activities.comics_creation;

import android.view.Menu;

import com.shevelev.comics_viewer.common.OptionsMenuManager;

public class SortPagesMenuManager extends OptionsMenuManager
{
    public SortPagesMenuManager(Menu menu)
    {
        super(menu);
    }

    public void setAcceptVisible(boolean visible)
    {
        setVisible(0, visible);
    }
}
