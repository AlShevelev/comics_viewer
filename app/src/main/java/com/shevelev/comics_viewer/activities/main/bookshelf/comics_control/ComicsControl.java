package com.shevelev.comics_viewer.activities.main.bookshelf.comics_control;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsInfo;
import com.shevelev.comics_viewer.activities.main.bookshelf.ComicsClickListener;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.helpers.StringsHelper;


public class ComicsControl extends LinearLayout
{
    private BookshelfComicsInfo comicsInfo;

    private static final int MAX_TEXT_LEN =35;

    public ComicsControl(Activity activity, BookshelfComicsInfo comicsInfo, int maxComicsWidth, IActionOneArgs<Long> onComicsChoosen)
    {
        super(activity);

        this.comicsInfo = comicsInfo;

        inflate(activity, R.layout.bookshelf_one_comics, this);

        ImageView coverImage = (ImageView)findViewById(R.id.comicsCoverImage);
        TextView coverText = (TextView)findViewById(R.id.comicsCoverText);

        String title = createComicsTitle(comicsInfo.getTitle());
        coverText.setText(title);
        coverImage.setImageBitmap(comicsInfo.getImage());

        ViewGroup.LayoutParams pc = new ViewGroup.LayoutParams(maxComicsWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(pc);

        setOnClickListener(new ComicsClickListener(comicsInfo.getId(), onComicsChoosen));

        if(comicsInfo.isPrivateAndClosed())     // To prevent long click on private&closed comics
            setOnLongClickListener(v -> true);

        activity.registerForContextMenu(this);
    }

    private String createComicsTitle(String sourceTitle)
    {
        return StringsHelper.cutToLength(sourceTitle, MAX_TEXT_LEN);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo()
    {
        ComicsContextMenuInfo comicsContextMenuInfo = new ComicsContextMenuInfo();
        comicsContextMenuInfo.dbId = comicsInfo.getId();
        comicsContextMenuInfo.menuTitle = comicsInfo.getTitle();

        return comicsContextMenuInfo;
    }
}