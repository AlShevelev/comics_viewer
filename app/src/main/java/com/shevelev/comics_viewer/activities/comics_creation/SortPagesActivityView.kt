package com.shevelev.comics_viewer.activities.comics_creation

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.shevelev.comics_viewer.common.structs.Size

/**
 * Context view for ComicsCreationActivity
 */
class SortPagesActivityView(context: Context?, layoutId: Int) : RelativeLayout(context) {
    val size: Size
        get() = Size(width, height)

    init {
        View.inflate(context, layoutId, this)
    }
}