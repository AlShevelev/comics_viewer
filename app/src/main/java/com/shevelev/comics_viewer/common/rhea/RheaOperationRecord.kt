package com.shevelev.comics_viewer.common.rhea

/**
 * One record with operation state
 */
class RheaOperationRecord(
    /**
     * Unique code of activity in witch this operation started
     */
    val activityCode: String,
    /**
     * Tag of operation
     */
    val tag: String, val operation: IRheaOperationForComposer) {
    /**
     * Result of work completed successfully
     */
    var result: Any? = null
    /**
     * Result of work completed with error
     */
    var error: Exception? = null
    /**
     * Current progress
     */
    var progressInfo: RheaOperationProgressInfo? = null

}