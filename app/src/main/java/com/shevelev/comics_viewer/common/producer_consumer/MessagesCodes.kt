package com.shevelev.comics_viewer.common.producer_consumer

/**
 * Codes of cross-threads messages
 */
internal object MessagesCodes {
    /**
     * Thread start stoping
     */
    const val StopThread = 1
    /**
     * Task completed
     */
    const val TaskCompleted = 2
    /**
     * There was an error while processig task
     */
    const val TaskError = 3
}