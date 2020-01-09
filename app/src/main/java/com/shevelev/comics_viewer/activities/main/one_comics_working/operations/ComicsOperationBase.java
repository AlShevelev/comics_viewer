package com.shevelev.comics_viewer.activities.main.one_comics_working.operations;

import android.app.Activity;

import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity;

/**
 * Base class for operations with comics
 */
abstract class ComicsOperationBase
{
    protected final IOneComicsActivity uiMethods;
    protected final Activity context;

    public ComicsOperationBase(IOneComicsActivity activity)
    {
        uiMethods = activity;
        context = (Activity)activity;
    }
}
