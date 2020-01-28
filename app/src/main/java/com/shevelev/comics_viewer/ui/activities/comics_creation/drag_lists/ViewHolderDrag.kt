package com.shevelev.comics_viewer.ui.activities.comics_creation.drag_lists

import android.widget.ImageView
import android.widget.TextView

/**
 * Temporary data struct for reusing views in ListAdapter
 */
class ViewHolderDrag {
    var Icon: ImageView? = null
    var ZoomIcon: ImageView? = null
    var VisibilityIcon: ImageView? = null
    var Text: TextView? = null
}