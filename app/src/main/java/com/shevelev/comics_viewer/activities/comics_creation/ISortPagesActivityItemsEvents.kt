package com.shevelev.comics_viewer.activities.comics_creation

interface ISortPagesActivityItemsEvents {
    /**
     * When user click on item's zoom icon
     */
    fun onZoomItem(itemIndex: Int)

    fun onSetVisibilityItem(itemIndex: Int)
}