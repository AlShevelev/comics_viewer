package com.shevelev.comics_viewer.common.producer_consumer

import android.os.Handler
import android.os.Message
import android.util.Log
import com.shevelev.comics_viewer.common.collections.ConcurrentSpillingStack
import com.shevelev.comics_viewer.common.func_interfaces.IFuncOneArg

/**
 * Base class for Producer/Consumer pattern. One consumer runing in background thread
 */
abstract class ProducerConsumerBase(maxQueueLen: Int) {
    private val tasks: ConcurrentSpillingStack<ProducerConsumerTaskBase>
    private val uiHandler // for pass message to uiThread
        : Handler
    private val workingThread // for pass message to uiThread
        : Thread

    private inner class Job : Runnable {
        /**
         * Main working method (runs in background thread)
         */
        override fun run() {
            while (true) {
                var taskToProcess: ProducerConsumerTaskBase? = null
                try {
                    taskToProcess = tasks.pop()
                    if (taskToProcess == null) // Queue is empty
                        synchronized(monitor) { monitor.wait() } else {
                        if (taskToProcess.kind == ProducerConsumerTaskKinds.Stop) {
                            val data = (taskToProcess as StopTask).data
                            uiHandler.sendMessage(Message.obtain(null, MessagesCodes.StopThread, data)) // Inform UI thread about stop processing
                            break
                        } else {
                            val result = processTask(taskToProcess)
                            if (result != null) uiHandler.sendMessage(Message.obtain(null, MessagesCodes.TaskCompleted, result)) // Inform UI thread about complete task
                            else uiHandler.sendMessage(Message.obtain(null, MessagesCodes.TaskError, taskToProcess))
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("ProducerConsumerBase", ex.message, ex)
                    if (taskToProcess != null) uiHandler.sendMessage(Message.obtain(null, MessagesCodes.TaskError, taskToProcess))
                }
            }
        }
    }

    /**
     * Start processing tasks (runs in UI thread)
     */
    fun start() {
        workingThread.start()
    }

    /**
     * Stop processing tasks  (runs in UI thread)
     */
    fun stop(dataToReturn: Any?) {
        val threadState = workingThread.state
        if (threadState == Thread.State.NEW || threadState == Thread.State.TERMINATED) onStopProcessing(dataToReturn)
        processAsync(StopTask(dataToReturn))
    }

    /**
     * Add task in processing queue  (runs in UI thread)
     */
    fun processAsync(task: ProducerConsumerTaskBase) {
        val spilledTask = tasks.push(task)
        synchronized(monitor) { monitor.notifyAll() }
        spilledTask?.let { onTaskSpilled(it) }
    }

    /**
     * Process task and return result (runs in UI thread)
     * @return result of task or null in case of error
     */
    fun processSync(task: ProducerConsumerTaskBase?): ProducerConsumerTaskProcessingResultBase? {
        return try {
            processTask(task)
        } catch (ex: Exception) {
            Log.e("ProducerConsumerBase", ex.message, ex)
            null
        }
    }

    /**
     * Add task in processing queue  (runs in UI thread)
     */
    fun removeTask(condition: IFuncOneArg<ProducerConsumerTaskBase, Boolean>) {
        tasks.remove(condition)
    }

    /**
     * Processing callback messages from working thread (runs in UI thread)
     * @param msg message from working thread
     * @return
     */
    private fun handleUIMessage(msg: Message): Boolean {
        when (msg.what) {
            MessagesCodes.StopThread -> onStopProcessing(msg.obj)
            MessagesCodes.TaskCompleted -> onTaskProcessed(msg.obj as ProducerConsumerTaskProcessingResultBase)
            MessagesCodes.TaskError -> onTaskError(msg.obj as ProducerConsumerTaskBase)
        }
        return true
    }

    /**
     * Method processing tasks (runs in background thread)
     * @param task
     * @return result of calculation or null in case of error
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    protected abstract fun processTask(task: ProducerConsumerTaskBase?): ProducerConsumerTaskProcessingResultBase?

    /**
     * Method called when processing stoped  (runs in UI thread)
     */
    protected abstract fun onStopProcessing(data: Any?)

    /**
     * Method called when processing stoped (runs in UI thread)
     */
    protected abstract fun onTaskProcessed(result: ProducerConsumerTaskProcessingResultBase?)

    /**
     * Method called when there was an error while task calculation (runs in UI thread)
     */
    protected abstract fun onTaskError(task: ProducerConsumerTaskBase?)

    /**
     * Method called when task was spilled from processing queue (runs in UI thread)
     */
    protected abstract fun onTaskSpilled(task: ProducerConsumerTaskBase?)

    companion object {
        private val monitor = java.lang.Object() // for thread-safe
    }

    /**
     *
     */
    init {
        tasks = ConcurrentSpillingStack(maxQueueLen)
        uiHandler = Handler(Handler.Callback { msg -> handleUIMessage(msg) })
        workingThread = Thread(Job())
    }
}