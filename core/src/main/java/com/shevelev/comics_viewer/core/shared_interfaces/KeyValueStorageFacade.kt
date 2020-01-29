package com.shevelev.comics_viewer.core.shared_interfaces

interface KeyValueStorageFacade {
    fun savePasswordEntered(value: Boolean)
    fun getPasswordEntered(): Boolean?

    fun savePassword(value: String)
    fun getPassword(): String?

    fun savePasswordHint(value: String)
    fun getPasswordHint(): String?
}