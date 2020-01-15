package com.shevelev.comics_viewer.common.rhea

import android.app.Activity
import java.util.*

/**
 * Core of management logic
 */
class RheaComposer : IRheaOperationComposer {
    // List of activities
    private val activities : LinkedList<RheaActivityRecord> = LinkedList()

    // List of operations
    private val operations : LinkedList<RheaOperationRecord> = LinkedList()

    /**
     * Get unique code for activity
     */
    private fun getActivityCode(activity: Activity): String {
        return activity.localClassName
    }

    /**
     * Inform about operation progress
     */
    override fun onOperationProgress(tag: String, value: Int, total: Int) {
        val operationRecord = getOperationRecord(tag)
        val activityRecord = getActivityRecord(operationRecord!!.activityCode)
        // if activityRecord==null it means that activity was destroyed and operation progress is useless
        if (activityRecord != null) {
            operationRecord.progressInfo = RheaOperationProgressInfo(value, total) // store progress info
            if (activityRecord.currentState === RheaActivityStates.Active) activityRecord.activity!!.onRheaWorkProgress(tag, operationRecord.progressInfo!!) // show progress
        }
    }

    /**
     * Inform about operation completed successfully
     */
    override fun onOperationCompleted(tag: String, result: Any) {
        val operationRecord = getOperationRecord(tag)
        val activityRecord = getActivityRecord(operationRecord!!.activityCode)
        if (activityRecord == null) // activity was destroyed
            removeOperationRecord(operationRecord) // so result is useless
        else {
            if (activityRecord.currentState === RheaActivityStates.Active) {
                activityRecord.activity!!.onRheaWorkCompleted(tag, result) // show result and remove operation
                removeOperationRecord(operationRecord)
            } else operationRecord.result = result // store result for future use
        }
    }

    /**
     * Inform about operation completed with error
     */
    override fun onOperationCompletedByError(tag: String, error: Exception) {
        val operationRecord = getOperationRecord(tag)
        val activityRecord = getActivityRecord(operationRecord!!.activityCode)
        if (activityRecord == null) // activity was destroyed
            removeOperationRecord(operationRecord) // so result is useless
        else {
            if (activityRecord.currentState === RheaActivityStates.Active) {
                activityRecord.activity!!.onRheaWorkCompletedByError(tag, error) // show result and remove operation
                removeOperationRecord(operationRecord)
            } else operationRecord.error = error // store result for future use
        }
    }

    /**
     * Run operation
     * @param operation
     * @return result of operation running
     */
    fun runOperation(activity: Activity, operation: RheaOperationBase): RheaOperationRunResult {
        val operationRecord = getOperationRecord(operation.tag)
        if (operationRecord != null) return RheaOperationRunResult.TagAlreadyRun
        val activityCode = getActivityCode(activity)
        val activityRecord = getActivityRecord(activityCode) ?: return RheaOperationRunResult.InvalidActivity
        val newOperationRecord = RheaOperationRecord(activityCode, operation.tag, operation)
        operation.setComposer(this)
        operations.add(newOperationRecord)
        operation.execute() // start operation in background
        return RheaOperationRunResult.Success
    }

    /**
     * Get progress about work
     * @param tag tag of work
     * @return progress of work (or null if operation not found)
     */
    fun getOperationProgress(tag: String): RheaOperationProgressInfo? {
        val operationRecord = getOperationRecord(tag)
        return operationRecord?.progressInfo
    }

    /**
     * Get operation state
     * @param tag tag of work
     * @return result of operation runing (or null if operation not found)
     */
    fun getOperationState(tag: String): RheaOperationState? {
        val operationRecord = getOperationRecord(tag) ?: return null
        return if (operationRecord.error == null && operationRecord.result == null) RheaOperationState.InProgress else RheaOperationState.Completed
    }

    /**
     * Registration activity in Rhea
     * @param activity
     */
    fun <T> onActivityCreate(activity: T) where T : Activity?, T : IRheaActivity? {
        val activityCode = getActivityCode(activity!!)
        var activityRecord = getActivityRecord(activityCode)
        if (activityRecord != null) // Activity was re-created
        {
            activityRecord.wasSaved = false
            activityRecord.activity = activity
        } else {
            activityRecord = RheaActivityRecord(activityCode, RheaActivityStates.Inactive, false, activity)
            activities.add(activityRecord)
        }
        for (operationRecord in operations)  // Inform UI about not completed activities
            if (operationRecord.activityCode == activityCode)
                if (operationRecord.result != null && operationRecord.error != null)
                    activityRecord.activity!!.onRheaWorkInit(operationRecord.tag, operationRecord.progressInfo!!)
    }

    /**
     * Inform about pausing activity
     * @param activity
     */
    fun onActivityPause(activity: Activity) {
        val activityRecord = getActivityRecord(getActivityCode(activity))
        if (activityRecord != null) activityRecord.currentState = RheaActivityStates.Inactive
    }

    /**
     * Inform about resuming activity
     * @param activity
     */
    fun onActivityResume(activity: Activity) {
        val activityCode = getActivityCode(activity)
        val activityRecord = getActivityRecord(activityCode) ?: return
        activityRecord.currentState = RheaActivityStates.Active
        activityRecord.wasSaved = false
        val completedOperations = ArrayList<RheaOperationRecord>(operations.size)
        for (operationRecord in operations) {
            if (operationRecord.activityCode == activityCode) // if operations was completed - tall about it and remove they
            {
                if (operationRecord.result != null) {
                    activityRecord.activity!!.onRheaWorkCompleted(operationRecord.tag, operationRecord.result!!)
                    completedOperations.add(operationRecord)
                } else if (operationRecord.error != null) {
                    activityRecord.activity!!.onRheaWorkCompletedByError(operationRecord.tag, operationRecord.error!!)
                    completedOperations.add(operationRecord)
                }
            }
        }
        for (operationRecord in completedOperations)  // remove completed operatios
            removeOperationRecord(operationRecord)
    }

    /**
     * Inform about saving activity state
     */
    fun onActivitySaveInstanceState(activity: Activity) {
        val activityRecord = getActivityRecord(getActivityCode(activity))
        if (activityRecord != null) activityRecord.wasSaved = true
    }

    /**
     * Inform about destroing activity
     * @param activity
     */
    fun onActivityDestroy(activity: Activity) {
        val activityRecord = getActivityRecord(getActivityCode(activity))
        if (!activityRecord!!.wasSaved) // If state was not saved it means that activity was destroyed completely
            removeActivityRecord(activityRecord)
    }

    private fun getOperationRecord(tag: String): RheaOperationRecord? {
        for (record in operations) if (record.tag == tag) return record
        return null // not found
    }

    private fun getActivityRecord(activityCode: String): RheaActivityRecord? {
        for (record in activities) if (record.code == activityCode) return record
        return null // not found
    }

    private fun removeOperationRecord(record: RheaOperationRecord?) {
        record!!.operation.setComposer(null) // tear link with composer
        operations.remove(record)
    }

    private fun removeActivityRecord(record: RheaActivityRecord?) {
        record!!.activity = null
        activities.remove(record)
    }

}