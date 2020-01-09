package com.shevelev.comics_viewer.activities.comics_creation;

public interface ISortPagesActivityItemsEvents
{
    /**
     * When user click on item's zoom icon
     */
    void onZoomItem(int itemIndex);

    void onSetVisibilityItem(int itemIndex);
}
