package com.shevelev.comics_viewer.ui.activities.folders.list_adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import com.shevelev.comics_viewer.ui.activities.folders.IActivityFoldersActions
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTree
import com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree.FoldersTreeItem
import com.shevelev.comics_viewer.ui.activities.folders.list_adapter.create_view.*
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemTypes
import java.util.*

/**
 * Draws one item in folders list
 */
class FoldersListAdapter(
    private val context: Activity,
    private val diskItems: List<DiskItemInfo>,
    private val actions: IActivityFoldersActions,
    foldersTree: FoldersTree
) : BaseAdapter(), IAdapterFoldersActions {

    companion object {
        private var inflater: LayoutInflater? = null
    }

    private val viewMap: HashMap<Int, View?>
    private var parentViewWidth = 0

    private var checkedDiskItem: DiskItemInfo?
    private var checkedCheckBox: CheckBox?
    private val foldersTreeFlatten: HashMap<String, FoldersTreeItem>?
    private val viewCreators: HashMap<DiskItemTypes, ICreateView?>

    init {
        foldersTreeFlatten = foldersTree.MakeFlatten()
        checkedDiskItem = null
        checkedCheckBox = null
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        viewMap = HashMap(diskItems.size)
        viewCreators = HashMap(5)
    }

    fun setParentViewWidth(width: Int) {
        parentViewWidth = width
    }

    override fun getCount(): Int {
        return diskItems.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (viewMap.containsKey(position)) viewMap[position]!! else {
            val diskItem = diskItems[position]
            val foldersTreeItem = foldersTreeFlatten!![diskItem.absolutePath]
            val createView = getViewCreator(diskItem)
            val rowView = createView!!.CreateView(diskItem, foldersTreeItem)
            viewMap[position] = rowView
            rowView!!
        }
    }

    override fun folderCheckedChange(checkBox: CheckBox, diskItem: DiskItemInfo) {
        if (checkedDiskItem == null) {
            checkedDiskItem = diskItem // Check by user
            checkedCheckBox = checkBox
            actions.FolderCheckChanged(diskItem.id, true)
        } else {
            if (checkedDiskItem === diskItem) {
                checkedDiskItem = null // Uncheck by user
                checkedCheckBox = null
                actions.FolderCheckChanged(diskItem.id, false)
            } else {
                checkedCheckBox!!.isChecked = false // Switch
                checkedDiskItem = diskItem
                checkedCheckBox = checkBox
                actions.FolderCheckChanged(diskItem.id, true)
            }
        }
    }

    private fun getViewCreator(diskItem: DiskItemInfo): ICreateView? {
        val diskItemType = diskItem.itemType
        var creator: ICreateView? = null
        creator = viewCreators[diskItemType]
        if (creator == null) creator = when (diskItemType) {
            DiskItemTypes.File -> {
                CreateFileView(context, inflater, parentViewWidth)
            }
            DiskItemTypes.Image -> {
                CreateImageView(context, inflater, parentViewWidth)
            }
            DiskItemTypes.Folder -> {
                CreateFolderView(context, inflater, parentViewWidth, actions, this)
            }
            DiskItemTypes.Device -> {
                CreateDeviceView(context, inflater, parentViewWidth, actions)
            }
            DiskItemTypes.SdCard -> {
                CreateSdCardView(context, inflater, parentViewWidth, actions)
            }
        }
        viewCreators[diskItemType] = creator
        return creator
    }
}