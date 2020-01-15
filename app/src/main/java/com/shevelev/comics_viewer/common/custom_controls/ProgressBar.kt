package com.shevelev.comics_viewer.common.custom_controls

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.comics_viewer.R

/**
 * Custom progress bar
 */
class ProgressBar : LinearLayout {
    private var text: TextView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.progress_bar_control, this)
        text = findViewById<View>(R.id.progressControlText) as TextView
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.INVISIBLE
    }

    fun setText(text: String?) {
        this.text!!.text = text
    }
}