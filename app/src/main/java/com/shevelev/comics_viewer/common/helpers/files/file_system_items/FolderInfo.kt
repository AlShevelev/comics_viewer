package com.shevelev.comics_viewer.common.helpers.files.file_system_items

import android.util.Log
import java.io.File
import java.util.*

/**
 * All data about folder
 */
class FolderInfo(val path: String) {

    var name: String = ""
        private set

    private var _subFolders: MutableList<DiskItemInfo>? = null
    val subFolders: List<DiskItemInfo>?
        get() = _subFolders

    private var _images: MutableList<DiskItemInfo> = mutableListOf()
    val images: List<DiskItemInfo>
        get() = _images

    private var _files // All files but not images
        : MutableList<DiskItemInfo>? = null

    val files: List<DiskItemInfo>?
        get() = _files

    private fun initDiskItems() {
        try {
            val root = File(path)
            name = root.name
            val files = root.listFiles()
            if (files == null) {
                _subFolders = mutableListOf()
                _images = mutableListOf()
                _files = mutableListOf()
            } else {
                _subFolders = ArrayList(files.size)
                _images = ArrayList(files.size)
                _files = ArrayList(files.size)
            }
            var index = 0
            for (file in files!!) {
                if (file.isHidden) continue
                val name = file.name
                if (file.isDirectory) {
                    _subFolders!!.add(DiskItemInfo(index++, DiskItemTypes.Folder, name, name, path + "/" + name))
                }
                else {
                    if (isImage(name)) {
                        _images!!.add(DiskItemInfo(index++, DiskItemTypes.Image, file.name, file.name, path)) }
                    else {
                        _files!!.add(DiskItemInfo(index++, DiskItemTypes.File, file.name, file.name, path))
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("", ex.message)
            throw ex
        }
    }

    private fun isImage(name: String?): Boolean {
        if (name == null || name === "") return false
        val lastDotIndex = name.lastIndexOf(".")
        if (lastDotIndex == -1) // There is not extention - it's a file
            return false
        val ext = name.substring(lastDotIndex + 1, name.length).toLowerCase()
        return _imagesExt.any { it == ext }
    }

    companion object {
        private val _imagesExt: List<String> = ArrayList(Arrays.asList("png", "jpg", "jpeg", "gif"))
    }

    init {
        initDiskItems()
    }
}