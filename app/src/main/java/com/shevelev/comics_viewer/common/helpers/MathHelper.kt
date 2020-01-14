package com.shevelev.comics_viewer.common.helpers

import com.shevelev.comics_viewer.common.structs.Pair
import java.util.*

object MathHelper {
    /**
     * Split interval on @parts equals parts
     * @param from - included
     * @param to - included
     * @param parts - quantity of parts
     */
    fun splitInterval(from: Int, to: Int, parts: Int): List<Pair<Int>> {
        require(from <= to) { "Argument:from must be less than Argument:to" }
        require(parts >= 1) { "Argument:parts can't be less than 1" }
        val result = ArrayList<Pair<Int>>(parts)
        if (parts == 1 || to - from <= 1) {
            result.add(Pair(from, to))
        } else {
            val sizeOfPart = (to - from) / parts
            var start = from
            for (i in 0 until parts) {
                val end = start + sizeOfPart // There will be some inaccuracy for the last part (but it's not important now)
                result.add(Pair(start, end))
                start = end
            }
        }
        return result
    }
}