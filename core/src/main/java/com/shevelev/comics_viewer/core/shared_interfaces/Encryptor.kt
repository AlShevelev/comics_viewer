package com.shevelev.comics_viewer.core.shared_interfaces

interface Encryptor {
    fun encrypt(data: ByteArray?): ByteArray?

    fun decrypt(data: ByteArray?): ByteArray?
}