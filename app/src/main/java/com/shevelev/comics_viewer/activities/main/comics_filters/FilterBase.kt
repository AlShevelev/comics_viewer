package com.shevelev.comics_viewer.activities.main.comics_filters

import android.content.Context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.ComicsSortInfo
import com.shevelev.comics_viewer.utils.sort
import com.shevelev.comics_viewer.dal.dto.Comics

/**
 * Base filter for comics
 */
abstract class FilterBase(
    private val context: Context,
    private val comicsSortInfo: ComicsSortInfo?,
    private val isPrivateComicsHidden: Boolean
) : IComicsFilter {
    override val comics: List<BookcaseComics>
        get() {
            var resultDraft = comicsList.toMutableList()
            if (comicsSortInfo != null) {
                resultDraft = resultDraft.sortedWith(comicsSortInfo.comparator).toMutableList()

                if(comicsSortInfo.isReverse) {
                    resultDraft.reverse()
                }

                resultDraft = resultDraft.sort(comicsSortInfo.comparator, comicsSortInfo.isReverse).toMutableList()
                return transform(resultDraft)
            }
            return listOf()
        }

    protected abstract val comicsList: List<Comics>

    private fun transform(source: List<Comics>): List<BookcaseComics> {
        val result = mutableListOf<BookcaseComics>()
        var nameCounter = 1
        for (sourceComics in source) {
            val targetComics = BookcaseComics()
            targetComics.id = sourceComics.id
            targetComics.name = sourceComics.name

            targetComics.displayName = if (isPrivateComicsHidden and sourceComics.isPrivate) {
                String.format(context.resources.getString(R.string.private_comics_title), nameCounter++) }
            else {
                sourceComics.name
            }

            targetComics.coverFilename = sourceComics.coverFilename
            targetComics.isNeedShowPrivateCover = isPrivateComicsHidden and sourceComics.isPrivate
            targetComics.creationDate = sourceComics.creationDate
            targetComics.lastViewDate = sourceComics.lastViewDate
            targetComics.lastViewedPageIndex = sourceComics.lastViewedPageIndex
            targetComics.totalPages = sourceComics.totalPages
            targetComics.isPrivate = sourceComics.isPrivate
            result.add(targetComics)
        }
        return result
    }

}