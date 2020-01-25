package com.shevelev.comics_viewer.key_value_storage.storages

/** Interface for commit operations of a storage */
interface StorageCommitOperations: StorageWriteOperations {
    /** Complete editing  */
    fun commit()
}