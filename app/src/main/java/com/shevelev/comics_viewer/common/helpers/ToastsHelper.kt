package com.shevelev.comics_viewer.common.helpers

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * Helper function for toasts
 */
object ToastsHelper {
    fun Show(context: Context, text: CharSequence?, duration: Duration, position: Position?) {
        val toast = Toast.makeText(context, text, if (duration == Duration.Long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        var gravity = 0
        when (position) {
            Position.Bottom -> gravity = Gravity.BOTTOM
            Position.Center -> gravity = Gravity.CENTER
            Position.Top -> gravity = Gravity.TOP
        }
        toast.setGravity(gravity, 0, 0)
        toast.show()
    }

    fun Show(context: Context, textResId: Int, duration: Duration, position: Position?) {
        Show(context, context.resources.getText(textResId), duration, position)
    }

    fun Show(context: Context, textResId: Int, position: Position?) {
        Show(context, textResId, Duration.Short, position)
    }

    fun Show(context: Context, text: CharSequence?, position: Position?) {
        Show(context, text, Duration.Short, position)
    }

    enum class Duration {
        Long, Short
    }

    enum class Position {
        Bottom, Center, Top
    }
}