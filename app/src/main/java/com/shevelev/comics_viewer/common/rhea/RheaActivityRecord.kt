package com.shevelev.comics_viewer.common.rhea

/**
 * One record with activity state
 */
class RheaActivityRecord(
    /**
     * Unique code of activity
     */
    val code: String,
    /**
     * State of activity
     */
    var currentState: RheaActivityStates,
    /**
     * Is method onSaveInstanceState was called for activity
     */
    var wasSaved: Boolean,

    var activity: IRheaActivity?
)