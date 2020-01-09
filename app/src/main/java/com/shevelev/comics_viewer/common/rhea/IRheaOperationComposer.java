package com.shevelev.comics_viewer.common.rhea;

/**
 * Interface of composer for operation
 */
interface IRheaOperationComposer
{
    /**
     * Inform about operation progress
     * @param tag tag of operation
     * @param value current progress value
     * @param total total progress items
     */
    void onOperationProgress(String tag, int value, int total);

    void onOperationCompleted(String tag, Object result);

    void onOperationCompletedByError(String tag, Exception exception);
}
