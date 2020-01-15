package com.shevelev.comics_viewer.common.rhea

import android.os.AsyncTask
import android.util.Log

/**
 * Base class for Rhea operation
 */
abstract class RheaOperationBase(val tag: String) : AsyncTask<Void?, Int?, Any?>(), IRheaOperationForComposer {
    private var composer: IRheaOperationComposer? = null
    private var processingException: Exception? = null

    /**
     * Set composer for operation
     * @param composer
     */
    override fun setComposer(composer: IRheaOperationComposer?) {
        this.composer = composer
    }

    protected override fun doInBackground(vararg params: Void?): Any? {
        return try {
            process()
        } catch (ex: Exception) {
            Log.e("RheaOperation (tag: $tag)", ex.message, ex)
            processingException = ex
            null
        }
    }

    protected override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        composer!!.onOperationProgress(tag, values[0]!!, values[1]!!)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        if (processingException != null) composer!!.onOperationCompletedByError(tag, processingException!!) else composer!!.onOperationCompleted(tag, result!!)
    }

    /**
     * Start method for processing operation (if it needed can throw exception)
     * @return result of operation (may be null)
     */
    protected abstract fun process(): Any?

    /**
     * Update progress if needed
     * @param value current progress value
     * @param total total progress items
     */
    protected fun updateProgress(value: Int, total: Int) {
        publishProgress(value, total)
    }

}