package com.shevelev.comics_viewer.common.producer_consumer;

/**
 * Codes of cross-threads messages
 */
class MessagesCodes
{
    /**
     * Thread start stoping
     */
    public static final int StopThread=1;

    /**
     * Task completed
     */
    public static final int TaskCompleted=2;

    /**
     * There was an error while processig task
     */
    public static final int TaskError=3;
}
