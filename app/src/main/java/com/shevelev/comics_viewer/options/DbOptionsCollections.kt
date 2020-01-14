package com.shevelev.comics_viewer.options

import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Option
import java.util.*

/**
 * Collection of innerOptionsCollection in memory and Db
 */
class DbOptionsCollections : OptionsCollections() {
    override fun addOrUpdate(options: Array<Option>) {
        val optionsToAdd = ArrayList<Option>(options.size)
        val optionsToUpdate = ArrayList<Option>(options.size)
        for (option in options) {
            if (get(option.key) == null) optionsToAdd.add(option) else optionsToUpdate.add(option)
        }
        if (DalFacade.Options.update(optionsToAdd, optionsToUpdate)) {
            for (option in optionsToAdd) innerOptionsCollection[option.key] = option.value!!
            for (option in optionsToUpdate) {
                innerOptionsCollection.remove(option.key)
                innerOptionsCollection[option.key] = option.value!!
            }
        }
    }

    override fun delete(keys: IntArray) {
        if (DalFacade.Options.delete(keys)) super.delete(keys)
    }

    override fun load() {
        val dbOptions = DalFacade.Options.all
        if (dbOptions != null) for (option in dbOptions) innerOptionsCollection[option!!.key] = option.value!!
    }

    init {
        load()
    }
}