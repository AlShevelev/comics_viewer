package com.shevelev.comics_viewer.activities.main.bookshelf;

import android.view.View;

import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;

public class ComicsClickListener implements View.OnClickListener
{
    private long dbComicsId;

    private IActionOneArgs<Long> onComicsChoosen;

    public ComicsClickListener(long dbComicsId, IActionOneArgs<Long> onComicsChoosen)
    {
        this.dbComicsId = dbComicsId;
        this.onComicsChoosen = onComicsChoosen;
    }

    @Override
    public void onClick(View v)
    {
        onComicsChoosen.process(dbComicsId);
    }
}
