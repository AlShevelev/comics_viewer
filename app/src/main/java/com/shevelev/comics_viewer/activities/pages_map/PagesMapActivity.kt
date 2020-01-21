package com.shevelev.comics_viewer.activities.pages_map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.ActivityCodes
import com.shevelev.comics_viewer.activities.ActivityResultCodes
import com.shevelev.comics_viewer.activities.view_comics.ActivityParamCodes
import com.shevelev.comics_viewer.common.dialogs.ZoomedPagePreviewDialog
import com.shevelev.comics_viewer.common.helpers.files.AppPrivateFilesHelper
import com.shevelev.comics_viewer.dal.DalFacade
import com.shevelev.comics_viewer.dal.dto.Page

/**
 * Activity for map of pages
 */
class PagesMapActivity : AppCompatActivity() {
    private var comicsId: Long = 0
    private var activePageIndex = 0
    private var view: PagesMapView? = null
    private var pages: List<Page>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comicsId = intent.extras.getLong(ActivityParamCodes.IdOfComics)
        val comics = DalFacade.Comics.getComicsById(comicsId)
        pages = DalFacade.Comics.getPages(comicsId)
        activePageIndex = comics!!.lastViewedPageIndex

        view = PagesMapView(
            this,
            comics,
            pages!!,
            { newActivePageIndex: Int -> onChangeActivePage(newActivePageIndex) },
            { pageIndex: Int -> onZoomPage(pageIndex) })
        setContentView(view)
    }

    /**
     * Change active page from PageControl
     */
    private fun onChangeActivePage(newActivePageIndex: Int) {
        if (DalFacade.Comics.updateLastViewedPageIndex(comicsId, newActivePageIndex)) {
            activePageIndex = newActivePageIndex
            view!!.updatePageControls(newActivePageIndex)
        }
    }

    /**
     * Click zoom icon in PageControl
     */
    private fun onZoomPage(pageIndex: Int) {
        val dialog = ZoomedPagePreviewDialog(
            this,
            AppPrivateFilesHelper.getFullName(pages!![pageIndex].fileName!!),
            view!!.size.scale(0.9f))
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_pages_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_close -> {
                prepareToFinish()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        prepareToFinish()
        super.onBackPressed() // call finish() and close acivity
    }

    private fun prepareToFinish() {
        val intent = Intent()
        intent.putExtra(ActivityResultCodes.ACTIVE_PAGE_INDEX, activePageIndex)
        setResult(Activity.RESULT_OK, intent)
    }

    companion object {
        /**
         * Start activity
         */
        fun start(parentActivity: Activity, comicsId: Long) {
            val intent = Intent(parentActivity, PagesMapActivity::class.java) // Start view comics
            val b = Bundle()
            b.putLong(ActivityParamCodes.IdOfComics, comicsId)
            intent.putExtras(b)
            parentActivity.startActivityForResult(intent, ActivityCodes.PAGES_MAP)
        }

        /**
         * Parse result of activity
         */
        fun parseResult(data: Intent): Int {
            return data.getIntExtra(ActivityResultCodes.ACTIVE_PAGE_INDEX, 0)
        }
    }
}