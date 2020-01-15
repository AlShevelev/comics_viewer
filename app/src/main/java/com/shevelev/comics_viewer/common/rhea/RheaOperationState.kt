package com.shevelev.comics_viewer.common.rhea

/**
 * State of operation
 */
enum class RheaOperationState {
    /**
     * Work completed (may be with error)
     */
    Completed,
    /**
     * Work in progress yet
     */
    InProgress
}