package com.shevelev.comics_viewer.dal.dto

import com.shevelev.comics_viewer.dal.entities.DbOption

/**
 * One innerOptionsCollection record
 */
class Option {
    var key: Int
    var value: String?

    constructor(key: Int, value: String) {
        this.key = key
        this.value = value
    }

    constructor(option: DbOption) {
        key = option.key
        value = option.value
    }
}