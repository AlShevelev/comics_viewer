package com.shevelev.comics_viewer.dal

import com.shevelev.comics_viewer.dal.dto.Option

/**
 * Dal for innerOptionsCollection
 */
interface IOptionsDal {
    /**
     * @return false - fail
     */
    fun update(optionsToAdd: List<Option?>?, optionsToUpdate: List<Option?>?): Boolean

    /**
     * @return false - fail
     */
    fun delete(keys: IntArray?): Boolean

    /**
     * @return null - fail
     */
    val all: List<Option?>?
}