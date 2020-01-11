package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.view.View

/**
 * Objects passed in Drag and Drop operation
 */
class PassObjectDrag internal constructor(val view: View, val item: ListItemDrag, val srcList: MutableList<ListItemDrag>)