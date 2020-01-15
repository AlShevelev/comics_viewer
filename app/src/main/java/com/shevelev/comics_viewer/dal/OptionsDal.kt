package com.shevelev.comics_viewer.dal

import android.util.Log
import com.activeandroid.ActiveAndroid
import com.activeandroid.Model
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.dal.dto.Option
import com.shevelev.comics_viewer.dal.entities.DbOption

/**
 * Dal for innerOptionsCollection
 */
class OptionsDal : IOptionsDal {
    /**
     * @return false - fail
     */
    private fun add(option: Option?) {
        DbOption(option!!).save()
    }

    /**
     * @return false - fail
     */
    private fun update(option: Option?) {
        val dbResult = Select().from(DbOption::class.java).where("Key = ?", option!!.key).execute<DbOption>()
        if (dbResult.size == 0) return
        val dbOption = dbResult[0]
        dbOption.value = option.value
        dbOption.save()
    }

    override fun update(optionsToAdd: List<Option?>?, optionsToUpdate: List<Option?>?): Boolean {
        ActiveAndroid.beginTransaction()
        return try {
            for (option in optionsToAdd!!) add(option)
            for (option in optionsToUpdate!!) update(option)
            ActiveAndroid.setTransactionSuccessful()
            true
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            false
        } finally {
            ActiveAndroid.endTransaction()
        }
    }

    /**
     * @return false - fail
     */
    override fun delete(keys: IntArray?): Boolean {
        ActiveAndroid.beginTransaction()
        return try {
            for (key in keys!!) Delete().from(DbOption::class.java).where("Key = ?", key).execute<Model>()
            true
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            false
        } finally {
            ActiveAndroid.endTransaction()
        }
    }

    /**
     * @return null - fail
     */
    override val all: List<Option>?
        get() = try {
            val dbResult = Select().from(DbOption::class.java).execute<DbOption>()
            CollectionsHelper.transform(dbResult) { item: DbOption? -> Option(item!!) }
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        }
}