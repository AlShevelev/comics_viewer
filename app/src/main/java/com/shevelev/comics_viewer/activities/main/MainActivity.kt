package com.shevelev.comics_viewer.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.ActivityCodes
import com.shevelev.comics_viewer.activities.UserActionsManager
import com.shevelev.comics_viewer.activities.comics_creation.SortPagesActivity
import com.shevelev.comics_viewer.activities.folders.ChooseFolderActivity
import com.shevelev.comics_viewer.activities.folders.ChooseFolderActivity.Companion.start
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsInfo
import com.shevelev.comics_viewer.activities.main.bookshelf.BookshelfComicsReader
import com.shevelev.comics_viewer.activities.main.bookshelf.comics_control.ComicsContextMenuInfo
import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsFilterFactory.getFilter
import com.shevelev.comics_viewer.activities.main.comics_filters.ComicsViewMode
import com.shevelev.comics_viewer.activities.main.one_comics_working.ComicsWorkingFacade
import com.shevelev.comics_viewer.activities.main.one_comics_working.IOneComicsActivity
import com.shevelev.comics_viewer.activities.main_options.MainOptionsActivity
import com.shevelev.comics_viewer.activities.view_comics.CurlActivity
import com.shevelev.comics_viewer.comics_workers.ComicsCreator
import com.shevelev.comics_viewer.comics_workers.ComicsDeletor
import com.shevelev.comics_viewer.common.helpers.ScreenHelper
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.rhea.IRheaActivity
import com.shevelev.comics_viewer.common.rhea.RheaFacade
import com.shevelev.comics_viewer.common.rhea.RheaOperationProgressInfo

class MainActivity : AppCompatActivity(), IRheaActivity, IOneComicsActivity {
    private var view: BookshelfView? = null
    private var changeModeHandler // Handle changing view of comics (All <--> Recent)
        : ChangeModeHandler? = null
    private var choosedContextComicsId // Id of comics choosed by context menu
        : Long? = null
    private var userActionsManager // For lock/unlock screen while user actions
        : UserActionsManager? = null
    private var comicsWorkingFacade: ComicsWorkingFacade? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RheaFacade.onCreate(this)
        userActionsManager = UserActionsManager(this)
        changeModeHandler = ChangeModeHandler(ComicsViewMode.ALL) { mode: ComicsViewMode -> comicsViewModeChanged(mode) }
        view = BookshelfView(this, R.layout.activity_main, changeModeHandler!!) { dbComicsId: Long -> onComicsChoosen(dbComicsId) }
        setContentView(view)
        comicsWorkingFacade = ComicsWorkingFacade(this)
        updateBooksList(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        RheaFacade.onDestroy(this)
    }

    override fun onPause() {
        super.onPause()
        RheaFacade.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        RheaFacade.onResume(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        RheaFacade.onSaveInstanceState(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    /**
     * Launch activity for choosing folder
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (userActionsManager!!.isActionsBlocked) return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_add -> {
                start(this) // Choose folder with comics
                return true
            }
            R.id.action_menu -> {
                MainOptionsActivity.start(this) // Show main innerOptionsCollection
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        val contextMenuInfo = menuInfo as ComicsContextMenuInfo
        choosedContextComicsId = contextMenuInfo.dbId
        menuInflater.inflate(R.menu.context_comics, menu)
        menu.setHeaderTitle(contextMenuInfo.menuTitle)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (userActionsManager!!.isActionsBlocked) super.onContextItemSelected(item)
        when (item.itemId) {
            R.id.comics_delete -> comicsWorkingFacade!!.delete.start(choosedContextComicsId!!)
            R.id.comics_edit -> comicsWorkingFacade!!.edit.start(choosedContextComicsId!!)
        }
        return super.onContextItemSelected(item)
    }

    /**
     * Processing result from child activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            ActivityCodes.CHOOSE_FOLDER -> comicsWorkingFacade!!.create.preStart(ChooseFolderActivity.parseResult(data))
            ActivityCodes.SORT_PAGES -> comicsWorkingFacade!!.create.start(SortPagesActivity.parseResult(data))
            ActivityCodes.CURL -> comicsWorkingFacade!!.view.complete(CurlActivity.parseResult(data))
            ActivityCodes.MAIN_OPTIONS -> {
                if (MainOptionsActivity.parseResult(data)) // Only if user enter password
                    updateBooksList(null)
            }
        }
    }

    /**
     * When user clicks on comics on shelve
     * @param dbComicsId - id of choosen comics
     */
    private fun onComicsChoosen(dbComicsId: Long) {
        comicsWorkingFacade!!.view.start(dbComicsId) // Start view comics
    }

    /**
     * Read comics and put them on the shelfs - in background thread
     * @param idOfComicsToScroll - id of comics to scroll to (if null - without scrolling)
     */
    override fun updateBooksList(idOfComicsToScroll: Long?) {
        val reader = BookshelfComicsReader(getFilter(changeModeHandler!!.viewMode),
            {
                userActionsManager!!.lock()
                view!!.showProgress()
            },
            { comics: List<BookshelfComicsInfo>? ->
                if (comics != null) {
                    view!!.setBooks(comics)
                    if (idOfComicsToScroll != null) {
                        view!!.scrollToComics(idOfComicsToScroll)
                    }
                } else {
                    ToastsHelper.Show(R.string.message_cant_read_comics, ToastsHelper.Position.Center)
                }

                view!!.hideProgress()
                userActionsManager!!.unlock()
            }, ScreenHelper.getClientSize(this))
        reader.execute()
    }

    /**
     * Is user actios turn on/off
     * @return true - actions locked
     */
    /**
     * Turn on/off user actios
     * @param isLocked if true - userActions locked
     */
    override var isUserActionsLock: Boolean
        get() = userActionsManager!!.isActionsBlocked
        set(isLocked) {
            if (isLocked) userActionsManager!!.lock() else userActionsManager!!.unlock()
        }

    /**
     * Show/hide progress
     * @param isVisible if true progress'll showed
     */
    override fun setProgressState(isVisible: Boolean) {
        if (isVisible) view!!.showProgress() else view!!.hideProgress()
    }

    /**
     * Update text of progress control
     * @param text
     */
    override fun updateProgressText(text: String) {
        view!!.setProgressText(text)
    }

    /**
     * Set view mode for bookcase (all comics or only recent)
     * @param viewMode
     */
    override fun setViewMode(viewMode: ComicsViewMode) {
        changeModeHandler!!.viewMode = viewMode
    }

    /**
     * When view mode of comics was changed
     */
    private fun comicsViewModeChanged(mode: ComicsViewMode) {
        updateBooksList(null)
    }

    /**
     * Work completed successfully
     */
    override fun onRheaWorkCompleted(tag: String, result: Any) {
        when (tag) {
            ComicsCreator.tag -> comicsWorkingFacade!!.create.complete(result)
            ComicsDeletor.tag -> comicsWorkingFacade!!.delete.complete()
        }
    }

    /**
     * There was an error while working
     */
    override fun onRheaWorkCompletedByError(tag: String, exception: Exception) {
        when (tag) {
            ComicsCreator.tag -> comicsWorkingFacade!!.create.completeWithError()
            ComicsDeletor.tag -> comicsWorkingFacade!!.delete.completeWithError()
        }
    }

    /**
     * Show work progress
     */
    override fun onRheaWorkProgress(tag: String, progressInfo: RheaOperationProgressInfo) {
        if (tag == ComicsCreator.tag) // Comics creation completed successfully
            comicsWorkingFacade!!.create.updateProgress(progressInfo)
    }

    /**
     * Call when activity restarts for every not completed work (so we should init view here)
     */
    override fun onRheaWorkInit(tag: String, progressInfo: RheaOperationProgressInfo) {
        if (tag == ComicsCreator.tag) comicsWorkingFacade!!.create.initOnRestart(progressInfo)
    }
}