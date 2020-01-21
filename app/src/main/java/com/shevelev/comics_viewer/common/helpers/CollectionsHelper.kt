package com.shevelev.comics_viewer.common.helpers

import com.shevelev.comics_viewer.common.threads.ICancelationTokenRead
import java.util.*

/**
 * Return filtered collection
 */
fun <T> List<T>?.filterOrNull(condition: (T) -> Boolean): List<T>? =
    if (this == null) null else (this as Iterable<T>).filter(condition)

/**
 * Returns first items for which condition is true, or null if such item is not exists
 */
fun <T> List<T>?.firstOrNull(condition: (T) -> Boolean): T? =
    if (this == null) null else (this as Iterable<T>).firstOrNull(condition)

/**
 * Returns index of first items for which condition is true, or null if such item is not exists
 */
fun <T> List<T>?.indexOfFirstOrNull(condition: (T) -> Boolean): Int? {
    if (this == null) return null

    val index = (this as Iterable<T>).indexOfFirst(condition)

    return if(index != -1) index else null
}

fun <TS, TT> List<TS>?.mapOrNull(transform: (TS) -> TT): List<TT>? =
    if(this == null) null else (this as Iterable<TS>).map(transform)

/**
 * Transfrom one list to another (wiht cancelaction)
 * @param cancelationToken - cancelation token to abort operation in other thread
 */
fun <TSource, TTarget> List<TSource>?.mapOrNull(cancelationToken: ICancelationTokenRead?, func: (TSource) -> TTarget): List<TTarget>? {
    if (cancelationToken != null && cancelationToken.isCanceled()) return null
    if (this == null) return null
    val result = mutableListOf<TTarget>()
    for (s in this) {
        if (cancelationToken != null && cancelationToken.isCanceled()) break
        result.add(func(s))
    }
    return result
}

/**
 * Take *quantityToTake* items from collection and skip *quantityToSkip*
 */
fun <T> List<T>?.takeOrNull(quantityToTake: Int, quantityToSkip: Int): List<T>? {
    if (this == null) return null
    if (quantityToSkip >= this.size) return listOf()
    val result = mutableListOf<T>()
    var count = 0
    var i = quantityToSkip
    while (i < this.size && count < quantityToTake) {
        result.add(this[i])
        count++
        i++
    }
    return result
}

/**
 * Sort collection
 * @param source source collection
 * @param comparator comparator to sort
 * @param reverse should pass True if we need reverse sort
 */
fun <T> List<T>.sort(comparator: kotlin.Comparator<T>, reverse: Boolean): List<T> {
    val target = this.sortedWith(comparator).toMutableList()

    //Collections.sort(target, comparator)
    if (reverse) {
        target.reverse()
    }
    return target
}