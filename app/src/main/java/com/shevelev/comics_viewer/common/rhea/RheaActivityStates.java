package com.shevelev.comics_viewer.common.rhea;

/**
 * State of activity
 */
public enum RheaActivityStates
{
    /**
     * Activity is active and can receive UI calls
     */
    Active,

    /**
     * Activity is not active and can't receive UI calls
     */
    Inactive
}
