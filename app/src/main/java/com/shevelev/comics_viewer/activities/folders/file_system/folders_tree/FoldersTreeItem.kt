package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import android.os.Parcel
import android.os.Parcelable
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead

/**
 *  One item of folders' tree
 *  [hasImages] The tree item has any image
 *  [isActive] The tree item has image in any subfolder
 *  [subItems] Child items
 */
class FoldersTreeItem(
    val type: FoldersTreeItemType,
    val absolutePath: String,
    var hasImages: Boolean,
    var isActive: Boolean,
    var subItems: List<FoldersTreeItem>?,
    val cancellationToken: ICancelationTokenRead?
) : Parcelable {

    constructor(type: FoldersTreeItemType, absolutePath: String, cancellationToken: ICancelationTokenRead):
        this(type, absolutePath, false, false, null, cancellationToken)

    /**
     * Init state of object
     */
    fun init() {
        if(cancellationToken!!.isCanceled())
            return

        val  folderInfo = FolderInfo(absolutePath)

        hasImages=!folderInfo.images.isNullOrEmpty()

        val itemsGetter = FoldersTreeCommonItemsGetter(folderInfo);
        val tempSubItems = itemsGetter.getSubItems(cancellationToken) ?: listOf()

        if(cancellationToken.isCanceled())
            return

        if(tempSubItems.isNullOrEmpty())           // It's a leaf
            subItems=null;
        else
        {
            val mutableSubItems = mutableListOf<FoldersTreeItem>()

            for(subItem in tempSubItems) {
                if(cancellationToken.isCanceled())
                    return

                subItem.init()

                if(subItem.hasImages || subItem.isActive)
                    mutableSubItems.add(subItem)
            }

            subItems = mutableSubItems
        }

        isActive = calculateIsActive()
    }

    private fun calculateIsActive(): Boolean {
        if(subItems.isNullOrEmpty())
            return false

        return subItems!!.any { it.isActive || it.hasImages }
    }

    constructor(parcel: Parcel) : this(
        FoldersTreeItemType.create(parcel.readInt()),
        parcel.readString()!!,
        parcel.readInt() == 1,
        parcel.readInt() == 1,
        parcel.createTypedArrayList(CREATOR),
        null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type.value)
        parcel.writeString(absolutePath)
        parcel.writeInt(if(hasImages) 1 else 0)
        parcel.writeInt(if(isActive) 1 else 0)
        parcel.writeTypedList(subItems)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FoldersTreeItem> {
        override fun createFromParcel(parcel: Parcel): FoldersTreeItem = FoldersTreeItem(parcel)

        override fun newArray(size: Int): Array<FoldersTreeItem?> = arrayOfNulls(size)
    }
}