package com.shevelev.comics_viewer.activities.comics_creation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.ActivityCodes
import com.shevelev.comics_viewer.activities.ActivityResultCodes
import com.shevelev.comics_viewer.activities.UserActionsManager
import com.shevelev.comics_viewer.activities.comics_creation.drag_lists.*
import com.shevelev.comics_viewer.activities.comics_creation.thumbnails.ThumbnailListIds
import com.shevelev.comics_viewer.activities.comics_creation.thumbnails.ThumbnailManager
import com.shevelev.comics_viewer.activities.view_comics.ActivityParamCodes
import com.shevelev.comics_viewer.common.ListViewHelper
import com.shevelev.comics_viewer.common.custom_controls.ProgressBar
import com.shevelev.comics_viewer.common.dialogs.ZoomedPagePreviewDialog
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.FolderInfo
import com.shevelev.comics_viewer.utils.filterOrNull
import com.shevelev.comics_viewer.utils.mapOrNull
import com.shevelev.comics_viewer.common.structs.Size
import com.shevelev.comics_viewer.core.cancelation.CancelationToken

/**
 * Activity for sorting pages on comics creation
 */
class SortPagesActivity : AppCompatActivity(), ISortPagesActivityItemsEvents {
    var items1: MutableList<ListItemDrag>? = null
    var items2: MutableList<ListItemDrag>? = null
    var listView1: ListView? = null
    var listView2: ListView? = null
    var myItemsListAdapter1: ListDragAdapter? = null
    var myItemsListAdapter2: ListDragAdapter? = null
    var area1: LinearLayoutDrag? = null
    var area2: LinearLayoutDrag? = null
    private var previewProgressBar: ProgressBar? = null
    private var menuManager: SortPagesMenuManager? = null

    private var resumeColor = 0 //Used to resume original color in drop ended/exited = 0

    private var createPreviewTask: CreatePreviewTask? = null
    private var isInited = false // Activiti initialized
    private var view: SortPagesActivityView? = null
    private var userActionsManager: UserActionsManager? = null
    private var thumbnailManager // to load pages images dynamicly
        : ThumbnailManager? = null
    //region CreatePreviewTask - Making preview in background thread
    /**
     * Making preview in background thread
     */
    internal inner class CreatePreviewTask : AsyncTask<Boolean?, Int?, Void?>() {
        private var pathToFolder: String = ""
        private var displaySize: Size? = null
        private var textPaint: Paint? = null
        private var isSuccess = false
        private var firstTime = false
        private val cancelationToken = com.shevelev.comics_viewer.core.cancelation.CancelationToken()
        private var listItemDragCreator: ListItemDragCreator? = null
        fun stopIfNeeded() {
            if (status != Status.FINISHED) {
                cancel(true)
                cancelationToken.cancel()
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            previewProgressBar!!.show()
            userActionsManager!!.lock()
            isSuccess = false
            pathToFolder = intent.extras.getString(ActivityParamCodes.PathToFolder)
            val metrics = resources.displayMetrics
            displaySize = Size(metrics.widthPixels, metrics.heightPixels)
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.sort_pages_list_item, null)
            val textView = view.findViewById<View>(R.id.rowTextView) as TextView
            textPaint = textView.paint // Paint of text area for measuring text size
            listItemDragCreator = ListItemDragCreator(this@SortPagesActivity, displaySize!!, textPaint!!)
            thumbnailManager = ThumbnailManager(listItemDragCreator!!, { dataToReturn: Any? -> onStopProcessingThumbnails(dataToReturn) })
        }

        protected override fun doInBackground(vararg params: Boolean?): Void? {
            try {
                if (cancelationToken.isCanceled()) return null
                firstTime = params[0]!!
                val calculatedItems = createListsItems(pathToFolder)

                items1 = calculatedItems.mapOrNull(cancelationToken) { it }!!.toMutableList()
                items2 = calculatedItems.mapOrNull(cancelationToken) { it }!!.toMutableList()

                thumbnailManager!!.warmUpCaches(items1!!)
                isSuccess = true
            } catch (e: Exception) {
                Log.e("", e.toString())
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (cancelationToken.isCanceled()) return
            if (isSuccess) {
                thumbnailManager!!.start()
                myItemsListAdapter1 = ListDragAdapter(this@SortPagesActivity, items1!!, R.color.drag, thumbnailManager!!, { dragInfo: ListItemDragingInfo -> onDragToList(dragInfo) }, ThumbnailListIds.LEFT)
                myItemsListAdapter2 = ListDragAdapter(this@SortPagesActivity, items2!!, R.color.drag, thumbnailManager!!, { dragInfo: ListItemDragingInfo -> onDragToList(dragInfo) }, ThumbnailListIds.RIGHT)
                listView1!!.adapter = myItemsListAdapter1
                listView2!!.adapter = myItemsListAdapter2
            } else ToastsHelper.Show(
                this@SortPagesActivity,
                R.string.message_box_no_cant_create_preview,
                ToastsHelper.Duration.Long,
                ToastsHelper.Position.Center)
            previewProgressBar!!.hide()
            if (firstTime) ToastsHelper.Show(
                this@SortPagesActivity,
                R.string.message_sorting_tip,  // Show tip about way of sorting
                ToastsHelper.Duration.Short,
                ToastsHelper.Position.Bottom)
            isInited = true
            userActionsManager!!.unlock()
        }

        /**
         * @return
         */
        private fun createListsItems(pathToFolderk: String?): List<ListItemDrag> {
            val result = mutableListOf<ListItemDrag>()
            if (cancelationToken.isCanceled()) return result
            val folderInfo = FolderInfo(pathToFolder)
            val images = folderInfo.images
            val totalImages = images.size
            for (i in 0 until totalImages) {
                if (cancelationToken.isCanceled()) break
                result.add(listItemDragCreator!!.create(images[i]))
            }
            return result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userActionsManager = UserActionsManager(this)
        view = SortPagesActivityView(this, R.layout.activity_sort_pages)
        setContentView(view)
        previewProgressBar = findViewById<View>(R.id.previewProgressBar) as ProgressBar
        listView1 = findViewById<View>(R.id.listviewLeft) as ListView
        listView1!!.divider = null
        listView1!!.dividerHeight = 0
        listView2 = findViewById<View>(R.id.listviewRight) as ListView
        listView2!!.divider = null
        listView2!!.dividerHeight = 0
        area1 = findViewById<View>(R.id.panelLeft) as LinearLayoutDrag
        area2 = findViewById<View>(R.id.panelRight) as LinearLayoutDrag
        area1!!.setOnDragListener(AreaOnDragListener( { dragInfo: ListItemDragingInfo -> onDragToArea(dragInfo) }))
        area2!!.setOnDragListener(AreaOnDragListener( { dragInfo: ListItemDragingInfo -> onDragToArea(dragInfo) }))
        area1!!.listView = listView1
        area2!!.listView = listView2
        val listener = ListItemOnClickTouchListener(this@SortPagesActivity, this@SortPagesActivity)
        listView1!!.onItemClickListener = listener
        listView1!!.setOnTouchListener(listener)
        listView2!!.onItemClickListener = listener
        listView2!!.setOnTouchListener(listener)
        listView1!!.onItemLongClickListener = ListItemLongClickListener()
        listView2!!.onItemLongClickListener = ListItemLongClickListener()
        resumeColor = resources.getColor(android.R.color.background_light)
        createPreviewTask = CreatePreviewTask()
        createPreviewTask!!.execute(savedInstanceState == null) // Create preview in background thread
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (createPreviewTask != null) createPreviewTask!!.stopIfNeeded()
        outState.putBoolean("isRestarted", true)
    }

    /**
     * Draging from list to empty area
     */
    private fun onDragToArea(dragInfo: ListItemDragingInfo) {
        dragInfo.sourceList.remove(dragInfo.draggedItem)
        dragInfo.destinationList.remove(dragInfo.draggedItem)
        dragInfo.sourceList.add(dragInfo.draggedItem)
        dragInfo.destinationList.add(dragInfo.draggedItem)
        dragInfo.sourceAdapter.notifyDataSetChanged()
        dragInfo.destinationAdapter.notifyDataSetChanged()
    }

    /**
     * Draging from list to list
     */
    fun onDragToList(dragInfo: ListItemDragingInfo) {
        val srcList = dragInfo.sourceList
        var destList = dragInfo.destinationList
        val srcAdapter = dragInfo.sourceAdapter
        var destAdapter: ListDragAdapter? = dragInfo.destinationAdapter

        if (srcList === destList) {
            destList = if (srcList === items1) items2!! else items1!!
            destAdapter = if (srcAdapter == myItemsListAdapter1) myItemsListAdapter2 else myItemsListAdapter1
        }
        srcList.remove(dragInfo.draggedItem)
        destList.remove(dragInfo.draggedItem)
        srcList.add(dragInfo.destinationLocation, dragInfo.draggedItem)
        destList.add(dragInfo.destinationLocation, dragInfo.draggedItem)
        srcAdapter.notifyDataSetChanged()
        destAdapter!!.notifyDataSetChanged()
    }

    /**
     * When user click on item's zoom icon
     */
    override fun onZoomItem(itemIndex: Int) {
        val item = items1!![itemIndex] // List have same orders of items
        val dialog = ZoomedPagePreviewDialog(
            this,
            item.fullPathToImageFile,
            view!!.size.scale(0.9f))
        dialog.show()
    }

    override fun onSetVisibilityItem(itemIndex: Int) {
        val item = items1!![itemIndex] // Both list contant same items so we must update only one
        item.isVisibile = !item.isVisibile
        ListViewHelper.invalidateListItem(itemIndex, listView1!!)
        ListViewHelper.invalidateListItem(itemIndex, listView2!!)
        menuManager!!.setAcceptVisible(items1!!.any { it.isVisibile }) // all items hided - hide accept icon
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_sort_pages, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuManager = SortPagesMenuManager(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    /**
     * Menu on Action bar handlers
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_close -> {
                thumbnailManager!!.stop(false)
                return true
            }
            R.id.action_accept -> {
                thumbnailManager!!.stop(true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * When user press back button
     */
    override fun onBackPressed() {
        if (!isInited) return
        thumbnailManager!!.stop(false)
    }

    /**
     * When thumbnail processing stopedv - finish work
     */
    private fun onStopProcessingThumbnails(dataToReturn: Any?) {
        if (dataToReturn as Boolean) closeOk() else closeCancel() // Close activity if Back pressed
    }

    /**
     * Close with Cancel result
     */
    private fun closeCancel() {
        if (!isInited) return
        val intent = Intent() // Close current activity
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    /**
     * Close with Ok result
     */
    private fun closeOk() {
        if (!isInited) return

        val idsList = items1
            .filterOrNull { item: ListItemDrag -> item.isVisibile }
            .mapOrNull { it.id }

        val ids = IntArray(idsList!!.size)
        for (i in ids.indices) ids[i] = idsList[i]
        val intent = Intent() // Choose folder and close activity
        intent.putExtra(ActivityResultCodes.ID_OF_PAGES, ids)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        //endregion
        /**
         * Start activity
         */
        fun start(parentActivity: Activity, pathToFolder: String?) {
            val intent = Intent(parentActivity, SortPagesActivity::class.java)
            val b = Bundle()
            b.putString(ActivityParamCodes.PathToFolder, pathToFolder)
            intent.putExtras(b)
            parentActivity.startActivityForResult(intent, ActivityCodes.SORT_PAGES)
        }

        /**
         * Parse result of activity
         */
        fun parseResult(data: Intent): IntArray {
            return data.getIntArrayExtra(ActivityResultCodes.ID_OF_PAGES)
        }
    }
}