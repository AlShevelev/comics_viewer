package com.shevelev.comics_viewer.common.threads

/**
 * Created by Syleiman on 04.10.2015.
 */
interface ICancelationTokenRead {
    fun isCanceled(): Boolean
}