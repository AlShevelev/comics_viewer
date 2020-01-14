package com.shevelev.comics_viewer.activities.folders

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.shevelev.comics_viewer.R
import com.shevelev.comics_viewer.activities.ActivityCodes
import com.shevelev.comics_viewer.activities.ActivityResultCodes
import com.shevelev.comics_viewer.activities.UserActionsManager
import com.shevelev.comics_viewer.activities.folders.ChooseFolderActivity
import com.shevelev.comics_viewer.activities.folders.file_system.disk_items.DiskItemsNormalProcessor
import com.shevelev.comics_viewer.activities.folders.file_system.disk_items.DiskItemsRootProcessor
import com.shevelev.comics_viewer.activities.folders.file_system.folders_tree.FoldersTree
import com.shevelev.comics_viewer.common.helpers.CollectionsHelper
import com.shevelev.comics_viewer.common.helpers.ToastsHelper
import com.shevelev.comics_viewer.common.helpers.files.file_system_items.DiskItemInfo
import java.util.*
import java.util.function.Predicate

class ChooseFolderActivity : AppCompatActivity(), IActivityFoldersActions {

    private var view: FoldersView? = null
    private var diskItems : List<DiskItemInfo>? = null     // List of files and folders

    private var pathBar: TextView? = null

    private var foldersInDeep : Stack<String>? = null       // Set of folders in navigation order
    private var menuManager: ChooseFoldersOptionsMenuManager? = null

    private var choosedDiskItem: DiskItemInfo? = null

    private var isInited = false // Activiti initialized

    protected var foldersTree: FoldersTree? = null
    private var userActionsManager: UserActionsManager? = null
    private var initActivityTask: InitActivityBase? = null

    //region Init activity
    internal inner abstract class InitActivityBase : AsyncTask<Void?, Void?, Void?>() {
        fun stopIfNeeded() {
            if (status != Status.FINISHED) {
                cancel(true)
                foldersTree!!.cancel()
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            preExecute()
        }

        protected fun preExecute() {
            userActionsManager!!.lock()
            foldersInDeep = Stack()
            foldersTree = FoldersTree()
            view = FoldersView(this@ChooseFolderActivity, R.layout.activity_choose_folder, this@ChooseFolderActivity, foldersTree!!)
            setContentView(view)
            view!!.showProgress()
        }

        protected override fun doInBackground(vararg params: Void?): Void? {
            try {
                init()
            } catch (e: Exception) {
                Log.e("", e.toString())
            }
            return null
        }

        protected abstract fun init()
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            postExecute()
        }

        protected open fun postExecute() {
            view!!.hideProgress()
            showDiskItems(ROOT_FOLDER, true)
            isInited = true
            userActionsManager!!.unlock()
            ToastsHelper.Show(
                R.string.message_choose_folder_tip,  // Show tip about way of sorting
                ToastsHelper.Duration.Short,
                ToastsHelper.Position.Bottom)
        }
    }

    /**
     * Initialization of activity if it starts first time
     */
    internal inner class InitActivityFirstTime : InitActivityBase() {
        override fun init() {
            foldersTree!!.Create()
            foldersTree!!.MakeFlatten()
        }
    }

    /**
     * Initialization of restarting activity
     */
    internal inner class InitRestartingActivity(private val savedState: Bundle) : InitActivityBase() {
        override fun init() {
            foldersTree!!.Load(savedState)
            foldersTree!!.MakeFlatten()
        }

        override fun postExecute() {
            view!!.hideProgress()
            showDiskItems(ROOT_FOLDER, true)
            isInited = true
            userActionsManager!!.unlock()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userActionsManager = UserActionsManager(this)
        initActivityTask = null
        initActivityTask = if (savedInstanceState == null) InitActivityFirstTime() else if (!savedInstanceState.getBoolean("isInited")) InitActivityFirstTime() else InitRestartingActivity(savedInstanceState)
        initActivityTask!!.execute()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (initActivityTask != null) initActivityTask!!.stopIfNeeded()
        outState.putBoolean("isInited", isInited)
        if (isInited) // Save folders tree
            foldersTree!!.Save(outState)
    }

    /**
     * Show  sub-disk items for path
     * @param path
     */
    private fun showDiskItems(path: String, memorizePath: Boolean) {
        pathBar = findViewById<View>(R.id.tvPath) as TextView
        setPathText(path, path != ROOT_FOLDER)
        val diskItemsProcessor = if (path == ROOT_FOLDER) DiskItemsRootProcessor() else DiskItemsNormalProcessor(path)
        diskItems = diskItemsProcessor.diskItems
        view!!.updateDiskItems(diskItems!!)
        if (memorizePath) foldersInDeep!!.push(path)
    }

    private fun setPathText(path: String, measure: Boolean) {
        var path = path
        if (measure) {
            val paint = pathBar!!.paint
            val panelWidth = pathBar!!.measuredWidth
            var textWidth = paint.measureText(path).toInt()
            while (textWidth >= panelWidth) {
                path = path.substring(1)
                textWidth = paint.measureText(path).toInt()
            }
        }
        pathBar!!.text = path
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_choose_folder, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuManager = ChooseFoldersOptionsMenuManager(menu)
        menuManager!!.setVisible(true)
        menuManager!!.setAcceptVisible(false)
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
            R.id.action_back -> {
                onBackPressed()
                return true
            }
            R.id.action_close -> {
                closeCancel()
                return true
            }
            R.id.action_choose -> {
                closeOk(choosedDiskItem!!.absolutePath)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Close with cancelation
     */
    private fun closeCancel() {
        if (!isInited) return
        val intent = Intent() // Close current activity
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun closeOk(pathToFolder: String) {
        if (!isInited) return
        val intent = Intent() // Choose folder and close activity
        intent.putExtra(ActivityResultCodes.PATH_TO_FOLDER, pathToFolder)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * When user press back button
     */
    override fun onBackPressed() {
        if (!isInited) return
        foldersInDeep!!.pop()
        if (foldersInDeep!!.empty()) // If we are in root folder - close activity
            super.onBackPressed() else showDiskItems(foldersInDeep!!.peek(), false) // Show items of parent level
    }

    /**
     * Folder's ckeckbox was changed by User
     * @param id Id of folder
     */
    override fun FolderCheckChanged(id: Int, isChecked: Boolean) {
        menuManager!!.setAcceptVisible(isChecked) // Show or hide accept button
        choosedDiskItem = if (isChecked) CollectionsHelper.first(diskItems, Predicate { i: DiskItemInfo? -> i!!.id == id })  else null
    }

    /**
     * User taped on folder
     * @param id Id of folder
     * @param folderWithImages true if this folder containts images
     */
    override fun FolderTaped(id: Int, folderWithImages: Boolean) {
        val diskItem = CollectionsHelper.first(diskItems, Predicate { i: DiskItemInfo? -> i!!.id == id })
        menuManager!!.setAcceptVisible(false) // Hide accept button
        choosedDiskItem = null
        showDiskItems(diskItem!!.absolutePath, true) // show items of this folder
        if (folderWithImages) ToastsHelper.Show(R.string.message_tap_to_preview, ToastsHelper.Duration.Short, ToastsHelper.Position.Bottom)
    }

    companion object {
        private const val ROOT_FOLDER = "/" // Constant for root folder
        //endregion
        /**
         * Start activity
         */
        fun start(parentActivity: Activity) {
            val intent = Intent(parentActivity, ChooseFolderActivity::class.java)
            parentActivity.startActivityForResult(intent, ActivityCodes.CHOOSE_FOLDER)
        }

        /**
         * Parse result of activity
         */
        fun parseResult(data: Intent): String {
            return data.getStringExtra(ActivityResultCodes.PATH_TO_FOLDER)
        }
    }
}