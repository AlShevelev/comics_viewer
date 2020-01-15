package com.shevelev.comics_viewer.common.threads

import java.util.concurrent.locks.ReentrantLock

class CancelationToken : ICancelationTokenRead {
    private var isCanceled = false
    private val lock = ReentrantLock()

    override fun isCanceled(): Boolean {
        var isCanceled = false
        lock.lock()
        isCanceled = try {
            this.isCanceled
        } finally {
            lock.unlock()
        }
        return isCanceled
    }

    fun cancel() {
        lock.lock()
        try {
            isCanceled = true
        } finally {
            lock.unlock()
        }
    }
}