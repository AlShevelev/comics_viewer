package com.shevelev.comics_viewer.common.rhea

/**
 * Result of operation runing
 */
enum class RheaOperationRunResult {
    /**
     * Operation ran
     */
    Success,
    /**
     * Operation with such tag already ran
     */
    TagAlreadyRun,
    /**
     * Activity is not found
     */
    InvalidActivity
}