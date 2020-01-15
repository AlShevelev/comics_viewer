package com.shevelev.comics_viewer.common.collections

import java.util.*

/**
 * Thread-safe stack with spilling in case of oversizing
 * @param <E>
</E> */
class ConcurrentSpillingStack<E>(private val maxLen: Int) {
    private val internalList: LinkedList<E> = LinkedList()
    private var len: Int = 0
    /**
     * Push @newItem in a head of stack
     * @param newItem item to push
     * @return spilled item or null if last item was not spilled
     */
    @Synchronized
    fun push(newItem: E): E? {
        var spilledItem: E? = null
        if (len == maxLen) spilledItem = internalList.removeLast() else len++
        internalList.addFirst(newItem)
        return spilledItem
    }

    /**
     * Pop first element from head
     * @return element or null if stack is empty
     */
    @Synchronized
    fun pop(): E? {
        if (len == 0) return null
        val result = internalList.removeFirst()
        len--
        return result
    }

    /**
     * Remove item from list by condition
     */
    @Synchronized
    fun remove(condition: (E) -> Boolean) {
        if (len == 0) return
        for (item in internalList) if (condition(item)) internalList.remove(item)
    }
}