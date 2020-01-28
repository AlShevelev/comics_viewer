package com.shevelev.comics_viewer.ui.activities.view_comics

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.ui.activities.ActivityCodes
import com.shevelev.comics_viewer.ui.activities.ActivityResultCodes
import com.shevelev.comics_viewer.ui.activities.pages_map.PagesMapActivity
import com.shevelev.comics_viewer.common.dialogs.GotoPageDialog
import com.shevelev.comics_viewer.common.dialogs.GotoPageDialog.InputModel
import com.shevelev.comics_viewer.common.dialogs.GotoPageDialog.OutputModel
import com.shevelev.comics_viewer.dal.DalFacade

/**
 * Simple Activity for curl testing.
 *
 * @author harism
 */
class CurlActivity : AppCompatActivity() {
    private var curlView: CurlView? = null
    private var comicsId: Long = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comicsId = intent.extras.getLong(ActivityParamCodes.IdOfComics)
        setContentView(R.layout.activity_view_comics)
        val currentPageIndex = DalFacade.Comics.getComicsById(comicsId)!!.lastViewedPageIndex
        curlView = findViewById<View>(R.id.curl) as CurlView
        curlView!!.setPageProvider(PageProvider(this, comicsId))
        curlView!!.initCurrentPageIndex(currentPageIndex)
        curlView!!.setBackgroundColor(-0xdfd7d0)

        curlView!!.setCallbackHandlers(
            { pageIndex: Int -> onPageChanged(pageIndex) },
            { onShowMenu() }
        )

        // This is something somewhat experimental. Before uncommenting next
// line, please see method comments in CurlView.
// curlView.setEnableTouchPressure(true);
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
    }

    public override fun onPause() {
        super.onPause()
        curlView!!.onPause()
    }

    public override fun onResume() {
        super.onResume()
        curlView!!.onResume()
    }

    /**
     * When user curled to new page
     * @param currentPageIndex new page index
     */
    private fun onPageChanged(currentPageIndex: Int) {
        DalFacade.Comics.updateLastViewedPageIndex(comicsId, currentPageIndex)
    }

    /**
     * When we need show menu
     */
    private fun onShowMenu() {
        val colors = arrayOf<CharSequence>(getString(R.string.menuitem_gotopage), getString(R.string.menuitem_pages), getString(R.string.menuitem_close_comics))
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setItems(colors) { baseDialog: DialogInterface?, which: Int ->
            when (which) {
                0 -> {
                    processGotoPage()
                } // Goto page
                1 -> {
                    processPages()
                } // Pages
                2 -> {
                    onBackPressed()
                } // Close comics
            }
        }
        val dialog: Dialog = builder.show()
        dialog.setCanceledOnTouchOutside(true)
    }

    /**
     * Goto page action
     */
    private fun processGotoPage() {
        val comics = DalFacade.Comics.getComicsById(comicsId)
        val gotoPageDialog = GotoPageDialog(this,
            { result: OutputModel -> if (result.pageNumber != comics!!.lastViewedPageIndex) curlView!!.setCurrentPageIndex(result.pageNumber) },
            InputModel(comics!!.lastViewedPageIndex, comics.totalPages))
        gotoPageDialog.show()
    }

    private fun processPages() {
        PagesMapActivity.start(this, comicsId) // Start view pages
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(ActivityResultCodes.ID_OF_COMICS, comicsId)
        setResult(RESULT_OK, intent)
        super.onBackPressed() // call finish() and close acivity
    }

    /**
     * Processing result from child activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null || resultCode != RESULT_OK) return
        if (requestCode == ActivityCodes.PAGES_MAP) curlView!!.setCurrentPageIndex(PagesMapActivity.parseResult(data))
    }

    companion object {
        /**
         * Start activity
         */
        fun start(parentActivity: Activity, comicsId: Long) {
            val intent = Intent(parentActivity, CurlActivity::class.java) // Start view comics
            val b = Bundle()
            b.putLong(ActivityParamCodes.IdOfComics, comicsId)
            intent.putExtras(b)
            parentActivity.startActivityForResult(intent, ActivityCodes.CURL)
        }

        /**
         * Parse result of activity
         */
        fun parseResult(data: Intent): Long {
            return data.getLongExtra(ActivityResultCodes.ID_OF_COMICS, 0)
        }
    }
}