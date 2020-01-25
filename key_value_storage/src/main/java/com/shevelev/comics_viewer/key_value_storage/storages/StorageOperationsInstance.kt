package com.shevelev.comics_viewer.key_value_storage.storages

interface StorageOperationsInstance {
    /** Create proxy for read */
    fun createReadOperationsInstance(): StorageReadOperations

    /** Create proxy for read */
    fun createWriteOperationsInstance(): StorageCommitOperations
}