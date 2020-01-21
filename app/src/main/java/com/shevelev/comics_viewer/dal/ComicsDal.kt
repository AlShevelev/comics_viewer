package com.shevelev.comics_viewer.dal

import android.util.Log
import com.activeandroid.ActiveAndroid
import com.activeandroid.Model
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.shevelev.comics_viewer.dal.dto.Comics
import com.shevelev.comics_viewer.dal.dto.Page
import com.shevelev.comics_viewer.dal.entities.DbComics
import com.shevelev.comics_viewer.dal.entities.DbPage
import java.util.*

internal class ComicsDal : IComicsDal {
    override fun createComics(comics: Comics?, pages: List<Page?>?): Long? {
        ActiveAndroid.beginTransaction()
        return try {
            comics!!.lastViewedPageIndex = 0 // From first page
            val dbComics = DbComics(comics)
            dbComics.save()
            for (page in pages!!) {
                val dbPage = DbPage(page!!, dbComics)
                dbPage.save()
            }
            ActiveAndroid.setTransactionSuccessful()
            dbComics.id
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        } finally {
            ActiveAndroid.endTransaction()
        }
    }

    /**
     * Get unsorted list of all comics
     * @param returnAll - return all comics (false - not private only)
     * @return - set of comics or null in case of error
     */
    override fun getComics(returnAll: Boolean): List<Comics>? {
        return try {
            var dbResult: List<DbComics?>? = null
            dbResult = if (returnAll) Select().from(DbComics::class.java).execute() else Select().from(DbComics::class.java).where("IsHidden = ?", 0).execute() // boolean maps to integer!
            dbResult.map { Comics(it!!) }
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        }
    }

    /**
     * Get one comics by its Id
     * @return - one comics or null in case of error
     */
    override fun getComicsById(id: Long): Comics? {
        return try {
            val dbComics: DbComics = Model.load(DbComics::class.java, id)
            Comics(dbComics)
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        }
    }

    /**
     * Get unsorted list of comics' pages
     * @return
     */
    override fun getPages(comicsId: Long): List<Page>? {
        return try {
            val dbResult = Select().from(DbPage::class.java).where("Comics = ?", comicsId).execute<DbPage>()
            dbResult.map { Page(it!!) }
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            null
        }
    }

    override fun updateLastViewedPageIndex(comicsId: Long, lastViewedPageIndex: Int): Boolean {
        return update(comicsId, { comics: DbComics -> comics.lastViewedPageIndex = lastViewedPageIndex })
    }

    override fun updateLastViewDate(comicsId: Long, lastViewDate: Date?): Boolean {
        return update(comicsId, { comics: DbComics -> comics.lastViewDate = lastViewDate })
    }

    override fun updateNameAndHidden(comicsId: Long, name: String?, isHidden: Boolean): Boolean {
        return update(comicsId) { comics: DbComics ->
            comics.name = name
            comics.isHidden = isHidden
        }
    }

    private fun update(comicsId: Long, processor: (DbComics) -> Unit): Boolean {
        ActiveAndroid.beginTransaction()
        return try {
            val dbComics: DbComics = Model.load(DbComics::class.java, comicsId)
            processor(dbComics)
            dbComics.save()
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
     * Delete one comics by its id
     * @return - deleleted comics or null if unsuccess
     */
    override fun deleteComics(id: Long): Comics? {
        var result: Comics? = null
        ActiveAndroid.beginTransaction()
        try {
            result = getComicsById(id)
            result!!.pages = getPages(id)!!

            Delete().from(DbPage::class.java).where("Comics = ?", id).execute<Model>()
            Model.delete(DbComics::class.java, id)

            ActiveAndroid.setTransactionSuccessful()
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            return null
        } finally {
            ActiveAndroid.endTransaction()
        }
        return result
    }
}