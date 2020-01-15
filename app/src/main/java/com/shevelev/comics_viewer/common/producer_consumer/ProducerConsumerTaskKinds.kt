package com.shevelev.comics_viewer.common.producer_consumer

/**
 * Kinds of task
 */
enum class ProducerConsumerTaskKinds {
    Normal, /**
     * Service kind - for stop processing
     */
    Stop
}