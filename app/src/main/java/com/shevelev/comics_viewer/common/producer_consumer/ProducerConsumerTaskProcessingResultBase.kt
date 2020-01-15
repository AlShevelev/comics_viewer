package com.shevelev.comics_viewer.common.producer_consumer

/**
 * Base class for result of task
 */
abstract class ProducerConsumerTaskProcessingResultBase protected constructor(
    /**
     * TaskBase::id
     */
    val id: Int)