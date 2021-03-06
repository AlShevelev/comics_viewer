package com.shevelev.comics_viewer.ui.activities.folders.file_system.folders_tree

import android.os.Bundle
import java.util.*

/**
 * Creates tree of folders
 */
class FoldersTree {
    private var innerState: FoldersTreeInnerState?
    private val cancelationToken: com.shevelev.comics_viewer.core.cancelation.CancelationToken

    init {
        innerState = FoldersTreeInnerState()
        cancelationToken = com.shevelev.comics_viewer.core.cancelation.CancelationToken()
    }

    fun cancel() {
        cancelationToken.cancel()
    }

    fun Create(): List<FoldersTreeItem>? {
        if (cancelationToken.isCanceled()) return null
        if (innerState!!.tree == null) {
            val rootGetter: IFoldersTreeItemsGetter = FoldersTreeRootItemsGetter()
            val tempRootItems = rootGetter.getSubItems(cancelationToken)
            if (tempRootItems?.isEmpty() != false) {
                innerState!!.tree = mutableListOf()
            } else {
                innerState!!.tree = mutableListOf()
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
        if (subItems?.isNotEmpty() == true) for (subItem in subItems) {
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