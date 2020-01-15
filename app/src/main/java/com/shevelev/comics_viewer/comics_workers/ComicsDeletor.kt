package com.shevelev.comics_viewer.comics_workers

import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.common.rhea.RheaOperationBase
import com.shevelev.comics_viewer.dal.DalFacade

/**
 * Comics is deleted here
 */
class ComicsDeletor(
    tag: String,
    private val comicsId: Long
) : RheaOperationBase(tag) {

    override fun process(): Any? {
        val comics = DalFacade.Comics.deleteComics(comicsId)
        if (comics != null) if (AppPrivateFilesHelper.delete(comics.coverFilename)) for (page in comics.pages) AppPrivateFilesHelper.delete(page.fileName)
        return null
    }

    companion object {
        const val tag = "ComicsDeletionRhea"
    }

}