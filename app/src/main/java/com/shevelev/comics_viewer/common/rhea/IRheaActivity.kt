package com.shevelev.comics_viewer.common.rhea

/**
 * Interface of activity to support Rhea
 */
interface IRheaActivity {
    /**
     * Work completed successfully
     * @param tag tag of work
     * @param result result
     */
    fun onRheaWorkCompleted(tag: String, result: Any)

    /**
     * There was an error while working
     * @param tag tag of work
     * @param exception concrete exception
     */
    fun onRheaWorkCompletedByError(tag: String, exception: Exception)

    /**
     * Show work progress
     * @param tag tag of work
     * @param progressInfo Progress info
     */
    fun onRheaWorkProgress(tag: String, progressInfo: RheaOperationProgressInfo)

    /**
     * Call when activity restarts for every not completed work (so we should init view here)
     * @param tag tag of work
     * @param progressInfo Progress info
     */
    fun onRheaWorkInit(tag: String, progressInfo: RheaOperationProgressInfo)
}