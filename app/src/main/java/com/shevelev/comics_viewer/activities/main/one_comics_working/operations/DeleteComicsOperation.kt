package com.shevelev.comics_viewer.activities.main.one_comics_working.operations

import android.content.Context
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.comics_workers.ComicsDeletor
import com.shevelev.comics_viewer.common.dialogs.MessageBoxHelper
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.rhea.RheaFacade

/**
 * Operation for delete comics
 */
class DeleteComicsOperation(private val activity: IOneComicsActivity?) : ComicsOperationBase(activity!!) {
    fun start(comicsId: Long) {
        MessageBoxHelper.createYesNoDialog(context, context.getString(R.string.message_box_delete_query_title), context.getString(R.string.message_box_delete_query),
            {
                uiMethods.isUserActionsLock = true
                uiMethods.setProgressState(true)
                RheaFacade.run(context, ComicsDeletor(activity as Context, ComicsDeletor.tag, comicsId))
            }, null).show()
    }

    fun complete() {
        uiMethods.setProgressState(false) // this execute after comics creation
        uiMethods.updateBooksList(null)
        uiMethods.isUserActionsLock = false
    }

    fun completeWithError() {
        ToastsHelper.Show(activity as Context, R.string.message_cant_delete_comics_title, ToastsHelper.Position.Center)
    }
}