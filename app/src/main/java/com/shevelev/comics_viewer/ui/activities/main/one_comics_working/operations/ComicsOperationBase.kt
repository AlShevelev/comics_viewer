package com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations

import android.app.Activity
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.IOneComicsActivity

/**
 * Base class for operations with comics
 */
abstract class ComicsOperationBase(protected val uiMethods: IOneComicsActivity) {
    protected val context: Activity = uiMethods as Activity

}