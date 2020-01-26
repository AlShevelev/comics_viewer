package com.shevelev.comics_viewer.core.shared_interfaces

interface KeyValueStorageFacade {
    fun saveAESCryptoKey(key: ByteArray)
    fun getAESCryptoKey(): ByteArray?

    fun savePasswordEntered(value: Boolean)
    fun getPasswordEntered(): Boolean?

    fun savePassword(value: String)
    fun getPassword(): String?

    fun savePasswordHint(value: String)
    fun getPasswordHint(): String?
}