package com.shevelev.comics_viewer.common.helpers

import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs
import com.shevelev.comics_viewer.common.func_interfaces.IFuncOneArg
import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead
import java.util.*
import java.util.function.Predicate

object CollectionsHelper {
    fun <T> any(source: List<T>?, condition: Predicate<T>): Boolean {
        if (source == null) return false
        for (t in source) if (condition.test(t)) return true
        return false
    }

    /**
     * Return filtered collection
     */
    fun <T> where(source: List<T>?, condition: Predicate<T>): List<T>? {
        if (source == null) return null
        if (source.isEmpty()) return ArrayList(0)
        val result = ArrayList<T>(source.size)
        for (item in source) {
            if (condition.test(item)) result.add(item)
        }
        return result
    }

    fun <TSource, TTarget> transform(source: List<TSource>?, func: IFuncOneArg<TSource, TTarget>): List<TTarget>? {
        return transform(source, func, null)
    }

    /**
     * Transfrom one list to another (wiht cancelaction)
     * @param cancelationToken - cancelation token to abort operation in other thread
     */
    fun <TSource, TTarget> transform(source: List<TSource>?, func: IFuncOneArg<TSource, TTarget>, cancelationToken: ICancelationTokenRead?): List<TTarget>? {
        if (cancelationToken != null && cancelationToken.isCanceled()) return null
        if (source == null) return null
        val result = ArrayList<TTarget>(source.size)
        for (s in source) {
            if (cancelationToken != null && cancelationToken.isCanceled()) break
            result.add(func.process(s))
        }
        return result
    }

    /**
     * Process some action for every item of collection
     * @param source - processed collection
     * @param <TSource> - type of collection's items
     * @param action - processing action
    </TSource> */
    fun <TSource> forEach(source: List<TSource>, action: IActionOneArgs<TSource>) {
        for (s in source) action.process(s)
    }

    /**
     * Returns first items for which condition is true, or null if such item is not exists
     */
    fun <T> first(source: List<T>?, condition: Predicate<T>): T? {
        if (source == null) return null
        for (t in source) if (condition.test(t)) return t
        return null
    }

    /**
     * Returns index of first items for which condition is true, or null if such item is not exists
     */
    fun <T> firstIndexOf(source: List<T>?, condition: Predicate<T>): Int? {
        if (source == null) return null
        var index = 0
        for (t in source) {
            if (condition.test(t)) return index
            index++
        }
        return null
    }

    /**
     * Check - can we work with collection?
     */
    fun <T> isNullOrEmpty(source: List<T>?): Boolean {
        return source == null || source.size == 0
    }

    /**
     * Sort collection
     * @param source source collection
     * @param comparator comparator to sort
     * @param reverse should pass True if we need reverse sort
     */
    fun <T> sort(source: List<T>, comparator: kotlin.Comparator<T>, reverse: Boolean): List<T> {
        val target = source.sortedWith(comparator).toMutableList()

        //Collections.sort(target, comparator)
        if (reverse) {
            target.reverse()
        }
        return target
    }

    /**
     * Take *quantityToTake* items from collection and skip *quantityToSkip*
     */
    fun <T> take(source: List<T>?, quantityToTake: Int, quantityToSkip: Int): List<T>? {
        if (source == null) return null
        if (quantityToSkip >= source.size) return ArrayList(0)
        val result = ArrayList<T>(quantityToTake)
        var count = 0
        var i = quantityToSkip
        while (i < source.size && count < quantityToTake) {
            result.add(source[i])
            count++
            i++
        }
        return result
    }
}