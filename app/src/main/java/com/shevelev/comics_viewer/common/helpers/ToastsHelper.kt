package com.shevelev.comics_viewer.common.helpers

import android.view.Gravity
import android.widget.Toast
import com.shevelev.comics_viewer.App.Main.context

/**
 * Helper function for toasts
 */
object ToastsHelper {
    fun Show(text: CharSequence?, duration: Duration, position: Position?) {
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

    fun Show(textResId: Int, duration: Duration, position: Position?) {
        Show(context!!.resources.getText(textResId), duration, position)
    }

    fun Show(textResId: Int, position: Position?) {
        Show(textResId, Duration.Short, position)
    }

    fun Show(text: CharSequence?, position: Position?) {
        Show(text, Duration.Short, position)
    }

    enum class Duration {
        Long, Short
    }

    enum class Position {
        Bottom, Center, Top
    }
}