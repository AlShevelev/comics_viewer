package com.shevelev.comics_viewer.core.cancelation

interface ICancelationTokenRead {
    fun isCanceled(): Boolean
}