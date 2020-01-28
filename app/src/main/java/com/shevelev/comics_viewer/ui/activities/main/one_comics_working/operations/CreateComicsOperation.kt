package com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations

import android.content.Context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.comics_creation.ChooseComicsName
import com.shevelev.comics_viewer.ui.activities.comics_creation.PagesStartSorter.sort
import com.shevelev.comics_viewer.ui.activities.comics_creation.SortPagesActivity.Companion.start
import com.shevelev.comics_viewer.ui.activities.main.comics_filters.ComicsViewMode
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.comics_workers.ComicsCreator
import com.shevelev.comics_viewer.common.helpers.ScreenHelper
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.rhea.RheaFacade
import com.shevelev.comics_viewer.common.rhea.RheaOperationProgressInfo

/**
 * Operation for create comics
 */
class CreateComicsOperation(private val activity: IOneComicsActivity?) : ComicsOperationBase(activity!!) {
    private var chooseComicsName: ChooseComicsName? = null
    private var comicsCreator: ComicsCreator? = null
    fun preStart(pathToFolder: String) {

        chooseComicsName = ChooseComicsName(context) {
            name: String, isPrivate: Boolean, pathToComicsFolder: String -> onComicsNameChoose(name, isPrivate, pathToComicsFolder)
        }
        chooseComicsName!!.startCreate(pathToFolder) // Choose comics name
    }

    fun start(diskItemsSortedIds: IntArray) {
        comicsCreator!!.setDiskItems(diskItemsSortedIds)
        uiMethods.isUserActionsLock = true
        uiMethods.setProgressState(true)
        RheaFacade.run(context, comicsCreator)
    }

    fun complete(result: Any?) {
        uiMethods.setProgressState(false)
        if (result == null) // Shit happends
            ToastsHelper.Show(activity as Context, R.string.message_cant_create_comics_title, ToastsHelper.Position.Center) else {
            uiMethods.setViewMode(ComicsViewMode.ALL)
            uiMethods.updateBooksList(result as Long?)
        }
        uiMethods.isUserActionsLock = false
    }

    fun completeWithError() {
        ToastsHelper.Show(activity as Context, R.string.message_cant_create_comics_title, ToastsHelper.Position.Center)
    }

    fun updateProgress(progressInfo: RheaOperationProgressInfo) {
        uiMethods.updateProgressText(String.format(context.resources.getText(R.string.pages_processing_progress).toString(), progressInfo.value, progressInfo.total))
    }

    fun initOnRestart(progressInfo: RheaOperationProgressInfo) {
        uiMethods.isUserActionsLock = true
        uiMethods.setProgressState(true)
        updateProgress(progressInfo)
    }

    /**
     * When we choose comics name
     * @param name - name of comics
     * @param pathToFolder - path to comics folder
     */
    private fun onComicsNameChoose(name: String, isPrivateComics: Boolean, pathToFolder: String) {
        val images = sort(pathToFolder)
        comicsCreator = ComicsCreator(activity as Context, ComicsCreator.tag, name, isPrivateComics, images, ScreenHelper.getClientSize(context))
        start(context, pathToFolder) // Start pages sorting
    }
}