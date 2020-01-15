package com.shevelev.comics_viewer.common.rhea

/**
 * Interface of composer for operation
 */
interface IRheaOperationComposer {
    /**
     * Inform about operation progress
     * @param tag tag of operation
     * @param value current progress value
     * @param total total progress items
     */
    fun onOperationProgress(tag: String, value: Int, total: Int)

    fun onOperationCompleted(tag: String, result: Any)
    fun onOperationCompletedByError(tag: String, exception: Exception)
}