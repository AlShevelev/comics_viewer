package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import android.os.Parcel
import android.os.Parcelable
import java.util.*


class FoldersTreeInnerState() : Parcelable {
    var tree: MutableList<FoldersTreeItem>? = null

    // Key: absolute path
    var flattenTree : HashMap<String, FoldersTreeItem>? = null

    constructor(parcel: Parcel) : this() {
        tree = parcel.createTypedArrayList(FoldersTreeItem.CREATOR).toMutableList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(tree)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FoldersTreeInnerState> {
        override fun createFromParcel(parcel: Parcel): FoldersTreeInnerState = FoldersTreeInnerState(parcel)

        override fun newArray(size: Int): Array<FoldersTreeInnerState?> = arrayOfNulls(size)
    }
}