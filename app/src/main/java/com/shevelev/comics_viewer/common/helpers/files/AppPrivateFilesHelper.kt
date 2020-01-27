package com.shevelev.comics_viewer.common.helpers.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Work with files in application private area
 */
object AppPrivateFilesHelper {
    /**
     * @param sourceAbsoluteFileName - source file full name (with path)
     * @param destinationFileName - destination file name WITHOUT PATH
     * @return true - if file created successfully
     */
    fun createFromFile(context: Context, sourceAbsoluteFileName: String?, destinationFileName: String?): Boolean {
        var sourceStream: FileReader? = null
        var destinationStream: FileOutputStream? = null
        return try {
            sourceStream = FileReader(sourceAbsoluteFileName)
            if (sourceStream.state == FileReader.State.ReadyToRead) {
                destinationStream = context.openFileOutput(destinationFileName, Context.MODE_PRIVATE)
                do {
                    val readedData = sourceStream.read()
                    if (readedData.size != 0) destinationStream.write(readedData)
                } while (sourceStream.state == FileReader.State.ReadyToRead)
                return sourceStream.state == FileReader.State.Final
            }
            false
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            false
        } finally {
            try {
                destinationStream?.close()
                sourceStream?.close()
            } catch (ex: IOException) {
                Log.e("CV", "exception", ex)
                return false
            }
        }
    }

    /**
     * @param destinationFileName - destination file name WITHOUT PATH (will createFromFile in private area)
     * @param quality - see com.syleiman.comicsviewer.Common.Helpers.Files.BitmapsQuality constants
     * @return true - if file created successfully
     */
    fun createFromBitmap(
        context: Context,
        destinationFileName: String?,
        bitmap: Bitmap,
        quality: Int,
        compressFormat: CompressFormat?): Boolean {
        var destinationStream: FileOutputStream? = null
        return try {
            destinationStream = context.openFileOutput(destinationFileName, Context.MODE_PRIVATE)
            bitmap.compress(compressFormat, quality, destinationStream)
            true
        } catch (ex: Exception) {
            Log.e("CV", "exception", ex)
            false
        } finally {
            try {
                destinationStream?.close()
            } catch (ex: IOException) {
                Log.e("CV", "exception", ex)
                return false
            }
        }
    }

    /**
     * @param sourceFileName - name of file in private area (without path)
     * @return bytes of file as array or null if reas was unsuccessed
     */
    @Throws(IOException::class)
    fun read(context: Context, sourceFileName: String?): ByteArray? {
        var sourceStream: FileReader? = null
        var destinationStream: ByteArrayOutputStream? = null
        return try {
            sourceStream = FileReader(context.openFileInput(sourceFileName))
            if (sourceStream.state == FileReader.State.ReadyToRead) {
                destinationStream = ByteArrayOutputStream(sourceStream.size.toInt())
                do {
                    val readedData = sourceStream.read()
                    if (readedData.size != 0) destinationStream.write(readedData)
                } while (sourceStream.state == FileReader.State.ReadyToRead)
                if (sourceStream.state == FileReader.State.Final) return destinationStream.toByteArray()
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            sourceStream?.close()
            destinationStream?.close()
        }
    }

    /**
     * Delete file in private area
     * @param fileName WITHOUT path
     * @return true if file was deleted
     */
    fun delete(context: Context, fileName: String?): Boolean {
        return context.deleteFile(fileName)
    }

    /**
     * Get full name of file in private area
     * @param fileName - name of file without path
     */
    fun getFullName(context: Context, fileName: String): String {
        return context.filesDir.absolutePath + "/" + fileName
    }
}