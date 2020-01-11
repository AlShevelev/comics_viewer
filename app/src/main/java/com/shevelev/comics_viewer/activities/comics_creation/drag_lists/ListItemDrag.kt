package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

/**
 * Item for page's sorting ListView
 */
class ListItemDrag(
        val id: Int,
        /**
         * Title (cutted if nessesary)
         */
        val itemString: String,
        /**
         * Title (uncutted)
         */
        val itemLongString: String,
        /**
         * Is page visible
         */
        var isVisibile: Boolean,
        /**
         * Full path to image
         */
        val fullPathToImageFile: String
)