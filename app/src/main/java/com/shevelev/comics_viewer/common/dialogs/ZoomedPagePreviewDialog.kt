package com.shevelev.comics_viewer.common.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper.loadFromFile
import com.shevelev.comics_viewer.common.helpers.BitmapsHelper.scale
import com.shevelev.comics_viewer.common.structs.Size

class ZoomedPagePreviewDialog(private val parentActivity: Activity, private val fullPathToImageFile: String, private val dialogSize: Size) {
    private val _initName: String? = null
    private val _okButton: Button? = null
    private var dialog: AlertDialog? = null

    private var imageSize: Size? = null
    private fun create(): AlertDialog {
        val inflater = parentActivity.layoutInflater
        val builder = AlertDialog.Builder(parentActivity)
        val view = inflater.inflate(R.layout.dialog_preview_zoomed, null) as FrameLayout
        val image = createBitmap()
        val pageImage = view.findViewById<View>(R.id.preview_page_image) as ImageView
        pageImage.setImageBitmap(image)
        val closeIcon = view.findViewById<View>(R.id.closeIcon) as ImageView
        closeIcon.setImageResource(R.drawable.ic_clear_yellow_48dp)
        closeIcon.setOnClickListener { v: View? -> dialog!!.dismiss() }
        builder.setView(view)
        return builder.create()
    }

    private fun createBitmap(): Bitmap {
        val source = loadFromFile(fullPathToImageFile)
        val sourceSize = Size(source.width, source.height)
        val targetSize = dialogSize.inscribe(sourceSize)
        imageSize = targetSize
        return scale(source, targetSize)
    }

    fun show() {
        dialog = create()
        dialog!!.show()
    }
}