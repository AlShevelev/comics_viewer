package com.shevelev.comics_viewer.options

import com.shevelev.comics_viewer.dal.dto.Option
import java.util.*

/**
 * Collection of innerOptionsCollection in memory
 */
open class OptionsCollections : IOptionsCollections {
    protected var innerOptionsCollection = TreeMap<Int, String>()
    /**
     * @return null if not found
     */
    override fun get(key: Int): String? {
        return innerOptionsCollection[key]
    }

    /**
     * @return false if fail
     */
    override fun addOrUpdate(options: Array<Option>) {
        for (option in options) {
            if (get(option.key) != null) delete(option.key)
            innerOptionsCollection[option.key] = option.value!!
        }
    }

    /**
     * @return false if fail
     */
    private fun delete(key: Int) {
        innerOptionsCollection.remove(key)
    }

    /**
     * @return false if fail
     */
    override fun delete(keys: IntArray) {
        for (key in keys) innerOptionsCollection.remove(key)
    }

    protected open fun load() {
        return  // Must be overriten in childs
    }
}