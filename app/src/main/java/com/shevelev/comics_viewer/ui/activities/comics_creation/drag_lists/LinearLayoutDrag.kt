package com.shevelev.comics_viewer.ui.activities.comics_creation.drag_lists

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ListView

/**
 * Extends standard LinearLayout to support dragging
 */
class LinearLayoutDrag
@JvmOverloads
constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    var listView: ListView? = null
}