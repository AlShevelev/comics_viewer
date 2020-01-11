package com.shevelev.comics_viewer.activities.comics_creation.drag_lists

/**
 * Information about draging from listener
 */
class ListItemDragingInfo(
    val draggedItem: ListItemDrag,
    /**
     * Drag from this list
     */
    val sourceList: MutableList<ListItemDrag>,
    /**
     * Drag to this list
     */
    val destinationList: MutableList<ListItemDrag>,
    /**
     * Drag from this agapter
     */
    val sourceAdapter: ListDragAdapter,
    /**
     * Drag to this adapter
     */
    val destinationAdapter: ListDragAdapter,
    /**
     * Index of item in target list
     */
    val destinationLocation: Int
)