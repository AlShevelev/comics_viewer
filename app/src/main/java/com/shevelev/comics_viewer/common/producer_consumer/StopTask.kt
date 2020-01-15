package com.shevelev.comics_viewer.common.producer_consumer

/**
 * Service task for stoping procssing
 */
internal class StopTask(val data: Any?) : ProducerConsumerTaskBase(-1, ProducerConsumerTaskKinds.Stop)