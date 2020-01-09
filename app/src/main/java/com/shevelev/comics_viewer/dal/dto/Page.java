package com.shevelev.comics_viewer.dal.dto;

import com.shevelev.comics_viewer.comics_workers.PreviewCreator;
import com.shevelev.comics_viewer.dal.entities.DbPage;

/**
 * One page of comics
 */
public class Page
{
    public long id;

    /** Filename without path */
    public String fileName;

    public int order;

    public boolean isLeftTopCornerDark;

    public boolean isLeftBottomCornerDark;

    public boolean isRightTopCornerDark;

    public boolean isRightBottomCornerDark;

    public Page()
    {
    }

    public Page(DbPage dbPage)
    {
        fileName = dbPage.fileName;
        order = dbPage.order;
        id = dbPage.getId();

        isLeftBottomCornerDark = dbPage.isLeftBottomCornerDark;
        isLeftTopCornerDark = dbPage.isLeftTopCornerDark;
        isRightBottomCornerDark = dbPage.isRightBottomCornerDark;
        isRightTopCornerDark = dbPage.isRightTopCornerDark;
    }

    public String getPreviewFileName()
    {
        return PreviewCreator.getPreviewFileName(fileName);
    }
}