package com.shevelev.comics_viewer.common.helpers.files

import java.io.BufferedInputStream
import java.io.FileInputStream

/**
 * Read data from file by portions
 */
class FileReader(fileStream: FileInputStream) {
    enum class State {
        /**
         * Ready to read new portion of data
         */
        ReadyToRead,
        /**
         * All data was readed
         */
        Final,
        /**
         * Ther was an error whire reading
         */
        Error
    }

    private var inputStream: BufferedInputStream? = null
    private val BUFFER_SIZE = 4096
    var state: State? = null
        private set

    /**
     * Get total size of file in bytes
     */
    var size: Long = 0

    constructor(fileName: String?) : this(FileInputStream(fileName)) {}

    /**
     * Reading next portion of data
     * @return readed data - not null, but must be checked on emptiness
     */
    fun read(): ByteArray {
        return if (state != State.ReadyToRead) emptyBuffer else try {
            val buffer = ByteArray(BUFFER_SIZE)
            val totalRead = inputStream!!.read(buffer)
            if (totalRead == -1) state = State.Final
            buffer
        } catch (e: Exception) {
            state = State.Error
            e.printStackTrace()
            close()
            emptyBuffer
        }
    }

    /**
     * Must call when finishing working with file
     */
    fun close() {
        try {
            if (inputStream != null) {
                inputStream!!.close()
                inputStream = null
                state = State.Final
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            state = State.Final
        }
    }

    private val emptyBuffer: ByteArray
        private get() = ByteArray(0)

    init {
        try {
            size = fileStream.channel.size()
            inputStream = BufferedInputStream(fileStream)
            state = State.ReadyToRead
        } catch (e: Exception) {
            state = State.Error
            e.printStackTrace()
        }
    }
}