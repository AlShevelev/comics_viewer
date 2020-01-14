package com.shevelev.comics_viewer.dal

import com.shevelev.comics_viewer.dal.dto.Comics
import com.shevelev.comics_viewer.dal.dto.Page
import java.util.*

interface IComicsDal {
    /**
     *
     * @return id of comics or null if save unsuccess
     */
    fun createComics(comics: Comics?, pages: List<Page?>?): Long?

    /**
     * Get unsorted list of all comics
     * @param returnAll - return all comics (false - public only)
     * @return - set of comics or null in case of error
     */
    fun getComics(returnAll: Boolean): List<Comics>?

    /**
     * Get one comics by its Id
     * @return - one comics or null in case of error
     */
    fun getComicsById(id: Long): Comics?

    /**
     * Get unsorted list of comics' pages
     * @return
     */
    fun getPages(comicsId: Long): List<Page>?

    fun updateLastViewedPageIndex(comicsId: Long, lastViewedPageIndex: Int): Boolean
    fun updateLastViewDate(comicsId: Long, lastViewDate: Date?): Boolean
    fun updateNameAndHidden(comicsId: Long, name: String?, isHidden: Boolean): Boolean
    /**
     * Delete one comics by its id
     * @return - one comics or null in case of error
     */
    fun deleteComics(id: Long): Comics?
}