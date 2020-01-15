package com.shevelev.comics_viewer.activities.folders.file_system.folders_tree

import android.os.Bundle
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.common.threads.CancelationToken
import java.util.*

/**
 * Creates tree of folders
 */
class FoldersTree {
    private var innerState: FoldersTreeInnerState?
    private val cancelationToken: CancelationToken

    init {
        innerState = FoldersTreeInnerState()
        cancelationToken = CancelationToken()
    }

    fun cancel() {
        cancelationToken.cancel()
    }

    fun Create(): List<FoldersTreeItem>? {
        if (cancelationToken.isCanceled()) return null
        if (innerState!!.tree == null) {
            val rootGetter: IFoldersTreeItemsGetter = FoldersTreeRootItemsGetter()
            val tempRootItems = rootGetter.getSubItems(cancelationToken)
            if (CollectionsHelper.isNullOrEmpty(tempRootItems)) innerState!!.tree = ArrayList(0) else {
                innerState!!.tree = ArrayList(tempRootItems!!.size)
                for (rootItem in tempRootItems) {
                    if (cancelationToken.isCanceled()) return null
                    rootItem.init()
                    if (rootItem.hasImages || rootItem.isActive) innerState!!.tree!!.add(rootItem)
                }
            }
        }
        return innerState!!.tree
    }

    fun MakeFlatten(): HashMap<String, FoldersTreeItem>? {
        if (cancelationToken.isCanceled()) return null
        if (innerState!!.tree == null) return null
        if (innerState!!.flattenTree == null) {
            innerState!!.flattenTree = HashMap()
            for (treeItem in innerState!!.tree!!) {
                if (cancelationToken.isCanceled()) return null
                MakeFlattenInternal(treeItem)
            }
        }
        return innerState!!.flattenTree
    }

    private fun MakeFlattenInternal(treeItem: FoldersTreeItem) {
        if (cancelationToken.isCanceled()) return
        innerState!!.flattenTree!![treeItem.absolutePath] = treeItem
        val subItems = treeItem.subItems
        if (!CollectionsHelper.isNullOrEmpty(subItems)) for (subItem in subItems!!) {
            if (cancelationToken.isCanceled()) return
            MakeFlattenInternal(subItem)
        }
    }

    fun Save(state: Bundle) {
        state.putParcelable("foldersTree", innerState)
    }

    fun Load(state: Bundle) {
        innerState = state.getParcelable("foldersTree")
        if (innerState == null) // Can't read - recreate
            Create()
    }
}