package com.shevelev.comics_viewer.options

import com.shevelev.comics_viewer.dal.dto.Option

/**
 * Created by Syleiman on 27.12.2015.
 */
interface IOptionsCollections {
    /**
     * @return null if not found
     */
    operator fun get(key: Int): String?

    /**
     * @return false if fail
     */
    fun addOrUpdate(options: Array<Option>)

    /**
     * @return false if fail
     */
    fun delete(keys: IntArray)
}