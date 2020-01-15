package com.shevelev.comics_viewer.common.producer_consumer

/**
 * Base class for task
 */
abstract class ProducerConsumerTaskBase protected constructor(
    /**
     * Id of task
     */
    val id: Int, val kind: ProducerConsumerTaskKinds = ProducerConsumerTaskKinds.Normal)