package com.shevelev.comics_viewer.key_value_storage.storages.in_memory

import com.shevelev.comics_viewer.core.di_scopes.ApplicationScope
import com.shevelev.comics_viewer.key_value_storage.storages.StorageBase
import com.shevelev.comics_viewer.key_value_storage.storages.StorageCommitOperations
import com.shevelev.comics_viewer.key_value_storage.storages.StorageReadOperations
import java.util.*
import javax.inject.Inject

/** Storage based on in-memory dictionary */
@ApplicationScope
class InMemoryStorage
@Inject
constructor(): StorageBase() {
    private val storage: MutableMap<String, Any> = TreeMap()

    /** Create proxy for read */
    override fun createReadOperationsInstance(): StorageReadOperations = InMemoryStorageOperations(storage)

    /** Create proxy for read */
    override fun createWriteOperationsInstance(): StorageCommitOperations = InMemoryStorageOperations(storage)
}