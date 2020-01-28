package com.shevelev.comics_viewer.key_value_storage.storages.combined

import com.shevelev.comics_viewer.core.di_scopes.Clarification
import com.shevelev.comics_viewer.key_value_storage.storages.StorageBase
import com.shevelev.comics_viewer.key_value_storage.storages.StorageCommitOperations
import com.shevelev.comics_viewer.key_value_storage.storages.StorageOperationsInstance
import com.shevelev.comics_viewer.key_value_storage.storages.StorageReadOperations
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import javax.inject.Named

/** Storage based on shared preferences and in-memory structure for cashing */
class CombinedStorage
@Inject
constructor(
    @Named(Clarification.IN_MEMORY) private val cacheStorage: StorageOperationsInstance,
    @Named(Clarification.PERSISTENT) private val persistentStorage: StorageOperationsInstance
): StorageBase() {

    private val lock = ReentrantReadWriteLock()

    /** Create proxy for read */
    override fun createReadOperationsInstance(): StorageReadOperations =
        CombinedStorageReadOperations(
            lock,
            persistentStorage.createReadOperationsInstance(),
            cacheStorage.createReadOperationsInstance(),
            cacheStorage.createWriteOperationsInstance())

    /** Create proxy for read */
    override fun createWriteOperationsInstance(): StorageCommitOperations =
        CombinedStorageUpdateOperations(
            lock,
            persistentStorage.createWriteOperationsInstance(),
            cacheStorage.createWriteOperationsInstance())
}