package com.shevelev.comics_viewer.ui.activities.main.one_comics_working

import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations.CreateComicsOperation
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations.DeleteComicsOperation
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations.EditComicsOperation
import com.shevelev.comics_viewer.ui.activities.main.one_comics_working.operations.ViewComicsOperation
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade

/**
 * Facade for working with one concrete comics
 */
class ComicsWorkingFacade(activity: IOneComicsActivity?, keyValueStorage: KeyValueStorageFacade) {
    var edit: EditComicsOperation = EditComicsOperation(activity)
    var delete: DeleteComicsOperation = DeleteComicsOperation(activity)
    var view: ViewComicsOperation = ViewComicsOperation(activity, keyValueStorage)
    var create: CreateComicsOperation = CreateComicsOperation(activity)
}