package com.shevelev.comics_viewer.common.collections

import com.shevelev.comics_viewer.common.func_interfaces.IFuncOneArg
import java.util.*

/**
 * Simple list with push and extract item operations
 */
open class DynamicList<E> {
    private val internalList: LinkedList<E> = LinkedList()

    /**
     * Push @newItem in a head of list
     */
    fun push(newItem: E) {
        internalList.addFirst(newItem)
    }

    /**
     * Get first item by condition and remove it from list
     * @return item or null if not found
     */
    fun extract(condition: IFuncOneArg<E, Boolean>): E? {
        for (item in internalList) {
            if (condition.process(item)) {
                internalList.remove(item)
                return item
            }
        }
        return null // Not found
    }

    /**
     * Is exists image with such conditions
     */
    fun isExists(condition: IFuncOneArg<E, Boolean>): Boolean {
        for (item in internalList) if (condition.process(item)) return true
        return false
    }
}