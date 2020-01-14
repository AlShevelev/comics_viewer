package com.shevelev.comics_viewer.activities.pages_map

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.dal.dto.Page
import java.util.*

class PageControl(
    activity: Activity,
    private val pageInfo: Page,
    maxPageWidth: Int,
    number: Int,
    activePageIndex: Int,
    onChangeActivePage: IActionOneArgs<Int>,
    onZoomPage: IActionOneArgs<Int>) : LinearLayout(activity) {
    private val pageIndex: Int
    private val activePageIndex: Int
    private val onChangeActivePage // Callback when active page changed (param: page index)
        : IActionOneArgs<Int>
    private val onZoomPage // Callback when click zoom icon (param: page index)
        : IActionOneArgs<Int>
    private var lastClickMoment: Date? = null
    private fun onClick(view: View) {
        if (needChangeCurrentPage()) onChangeActivePage.process(pageIndex) // change active page
    }

    /**
     * Calculate condition for changing current page
     */
    private fun needChangeCurrentPage(): Boolean {
        if (activePageIndex == pageIndex) return false
        if (lastClickMoment == null) {
            lastClickMoment = Date()
            ToastsHelper.Show(R.string.message_tap_again_to_change_page, ToastsHelper.Duration.Short, ToastsHelper.Position.Bottom)
        } else {
            val now = Date()
            val timeDelta = now.time - lastClickMoment!!.time
            if (timeDelta <= 1500) // 1s.
            {
                lastClickMoment = null
                return true
            } else {
                lastClickMoment = Date()
                ToastsHelper.Show(R.string.message_tap_again_to_change_page, ToastsHelper.Duration.Short, ToastsHelper.Position.Bottom)
            }
        }
        return false
    }

    private fun onZoomIconClick(view: View) {
        onZoomPage.process(pageIndex)
    }

    init {
        pageIndex = number - 1
        this.activePageIndex = activePageIndex
        this.onChangeActivePage = onChangeActivePage
        this.onZoomPage = onZoomPage
        View.inflate(activity, R.layout.pages_map_one_page, this)
        val pageText = findViewById<View>(R.id.pageText) as TextView
        pageText.text = String.format(activity.resources.getString(R.string.pageTitle), number)
        val coverImage = findViewById<View>(R.id.pageImage) as ImageView
        val bmp = BitmapsHelper.loadFromFile(AppPrivateFilesHelper.getFullName(pageInfo.previewFileName!!))
        coverImage.setImageBitmap(bmp)
        val zoomIcon = findViewById<View>(R.id.pageZoomIcon) as ImageView
        zoomIcon.setImageResource(R.drawable.ic_zoom_in_yellow_48dp)
        val rootView = findViewById<View>(R.id.rootControl) as LinearLayout
        if (activePageIndex != number - 1) // Set active page
            rootView.background = null
        val pc = ViewGroup.LayoutParams(maxPageWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams = pc
        setOnClickListener { v: View -> onClick(v) }
        zoomIcon.setOnClickListener { v: View -> onZoomIconClick(v) }
    }
}