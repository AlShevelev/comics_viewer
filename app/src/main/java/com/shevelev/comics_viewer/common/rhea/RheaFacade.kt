package com.shevelev.comics_viewer.common.rhea

import android.app.Activity

/**
 * Facade interface of Rhea
 */
object RheaFacade {
    private val composer = RheaComposer()
    /**
     * Run operation
     * @param operation
     * @return result of operation runing
     */
    fun run(activity: Activity?, operation: RheaOperationBase?): RheaOperationRunResult {
        return composer.runOperation(activity!!, operation!!)
    }

    /**
     * Get progress about work
     * @param tag tag of work
     * @return progress of work (or null if operation not found)
     */
    fun getProgress(tag: String?): RheaOperationProgressInfo? {
        return composer.getOperationProgress(tag!!)
    }

    /**
     * Get operation state
     * @param tag tag of work
     * @return result of operation runing (or null if operation not found)
     */
    fun getState(tag: String?): RheaOperationState? {
        return composer.getOperationState(tag!!)
    }

    /**
     * Registration activity in Rhea
     * @param activity
     */
    fun <T> onCreate(activity: T) where T : Activity?, T : IRheaActivity? {
        composer.onActivityCreate(activity)
    }

    /**
     * Inform about pausing activity
     * @param activity
     */
    fun onPause(activity: Activity?) {
        composer.onActivityPause(activity!!)
    }

    /**
     * Inform about resuming activity
     * @param activity
     */
    fun onResume(activity: Activity?) {
        composer.onActivityResume(activity!!)
    }

    /**
     * Inform about saving activity state
     */
    fun onSaveInstanceState(activity: Activity?) {
        composer.onActivitySaveInstanceState(activity!!)
    }

    /**
     * Inform about destroing activity
     * @param activity
     */
    fun onDestroy(activity: Activity?) {
        composer.onActivityDestroy(activity!!)
    }
}