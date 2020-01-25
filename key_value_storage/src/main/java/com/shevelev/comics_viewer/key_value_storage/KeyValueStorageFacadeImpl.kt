package com.shevelev.comics_viewer.key_value_storage

import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.key_value_storage.storages.Storage
import javax.inject.Inject

/**
 * Helper class for access to App-level private shared preferences
 */
class KeyValueStorageFacadeImpl
@Inject
constructor(
    private val keyValueStorage: Storage
) : KeyValueStorageFacade {

    /**
     * Save encrypted key for AES encoding (key is encrypted via RSA alg, it works for old devices only - prior to 23 API)
     */
    override fun saveAESCryptoKey(key: ByteArray) =
        keyValueStorage.update {
            it.putBytes("CRYPTO_KEY_AES", key)
        }

    /**
     * Get encrypted key for AES encoding (key is encrypted via RSA alg, it works for old devices only - prior to 23 API)
     */
    override fun getAESCryptoKey(): ByteArray? =
        keyValueStorage.read {
            it.readBytes("CRYPTO_KEY_AES")
        }
}