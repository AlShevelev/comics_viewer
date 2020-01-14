package com.shevelev.comics_viewer.dal.entities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.shevelev.comics_viewer.dal.dto.Option

/**
 * One innerOptionsCollection record
 */
@Table(name = "Options")
class DbOption : Model {
    @Column(name = "Key")
    var key = 0

    @Column(name = "Value")
    var value: String? = null

    constructor() {}
    constructor(option: Option) {
        key = option.key
        value = option.value
    }
}