package com.shevelev.comics_viewer.activities.main.one_comics_working

import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsViewMode

/**
 * Operation for working with one comics
 */
interface IOneComicsActivity {
    /**
     * Read comics and put them on the shelfs - in background thread
     * @param idOfComicsToScroll - id of comics to scroll to (if null - without scrolling)
     */
    fun updateBooksList(idOfComicsToScroll: Long?)

    /**
     * Is user actios turn on/off
     * @return true - actions locked
     */
    /**
     * Turn on/off user actios
     * @param isLocked if true - userActions locked
     */
    var isUserActionsLock: Boolean

    /**
     * Show/hide progress
     * @param isVisible if true progress'll showed
     */
    fun setProgressState(isVisible: Boolean)

    /**
     * Update text of progress control
     * @param text
     */
    fun updateProgressText(text: String)

    /**
     * Set view mode for bookcase (all comics or only recent)
     * @param viewMode
     */
    fun setViewMode(viewMode: ComicsViewMode)
}