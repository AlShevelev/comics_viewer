package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.comics_creation.thumbnails.ThumbnailListIds
import com.shevelev.comics_viewer.activities.comics_creation.thumbnails.ThumbnailManager
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs

/**
 * Adapter for pages sorting view
 */
class ListDragAdapter(
    private val activity: Activity,
    val list: MutableList<ListItemDrag>,
    private val dragColor: Int,
    private val thumbnailManager: ThumbnailManager,
    private val onDrag: IActionOneArgs<ListItemDragingInfo>,
    private val lisId: ThumbnailListIds) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        var viewHolder: ViewHolderDrag? = null

        if (rowView == null) // reuse views
        {
            val inflater = activity.layoutInflater
            rowView = inflater.inflate(R.layout.sort_pages_list_item, null)
            viewHolder = ViewHolderDrag()
            viewHolder.Icon = rowView.findViewById<View>(R.id.rowImageView) as ImageView
            viewHolder.ZoomIcon = rowView.findViewById<View>(R.id.rowZoomIcon) as ImageView
            viewHolder.VisibilityIcon = rowView.findViewById<View>(R.id.rowVisibilityIcon) as ImageView
            viewHolder.Text = rowView.findViewById<View>(R.id.rowTextView) as TextView
            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolderDrag
        }

        val listItem = list[position]
        val listThumbnailHolder = ListThumbnailHolder(listItem.id, lisId, listItem.fullPathToImageFile, viewHolder.Icon!!)
        thumbnailManager.getThumbnail(listThumbnailHolder) // Load image dynamicly
        viewHolder.ZoomIcon!!.setImageResource(R.drawable.ic_zoom_in_yellow_48dp)
        viewHolder.VisibilityIcon!!.setImageResource(getVisibilityIcon(listItem))
        viewHolder.Text!!.text = listItem.itemString

        rowView!!.setOnDragListener(ListItemOnDragListener(listItem, dragColor, onDrag))
        return rowView
    }

    private fun getVisibilityIcon(listItem: ListItemDrag): Int {
        return if (listItem.isVisibile) R.drawable.ic_visibility_yellow_48dp else R.drawable.ic_visibility_off_yellow_48dp
    }
}