package com.shevelev.comics_viewer.common.collections

import java.util.*

/**
 * Queue with spilling in case of oversizing
 * @param <E>
</E> */
class SpillingQueue<E>(private val maxLen: Int) {
    protected val internalList: LinkedList<E> = LinkedList()
    private var len: Int = 0
    /**
     * Push @newItem in a head of queue
     * @param newItem item to push
     * @return spilled item or null if last item was not spilled
     */
    fun push(newItem: E): E? {
        var spilledItem: E? = null
        if (len == maxLen) spilledItem = internalList.removeLast() else len++
        internalList.addFirst(newItem)
        return spilledItem
    }

    /**
     * Get first item by condition
     * @param condition
     * @return item or null if not found
     */
    operator fun get(condition: (E) -> Boolean): E? {
        if (len == 0) return null
        for (item in internalList) if (condition(item)) return item
        return null
    }

    /**
     * Get first item by condition and move it into head of list
     * @param condition
     * @return item or null if not found
     */
    fun getAndMoveToHead(condition: (E) -> Boolean): E? {
        if (len == 0) return null
        var result: E? = null
        var index = 0
        for (item in internalList) {
            if (condition(item)) {
                result = item
                break
            }
            index++
        }
        if (result != null) {
            internalList.removeAt(index)
            internalList.addFirst(result)
        }
        return result
    }
}