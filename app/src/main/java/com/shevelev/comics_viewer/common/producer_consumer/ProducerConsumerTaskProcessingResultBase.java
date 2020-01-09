package com.shevelev.comics_viewer.common.producer_consumer;

/**
 * Base class for result of task
 */
public abstract class ProducerConsumerTaskProcessingResultBase
{
    /**
     * TaskBase::id
     */
    private final int id;
    public int getId() { return id; }

    protected ProducerConsumerTaskProcessingResultBase(int id)
    {
        this.id = id;
    }
}
