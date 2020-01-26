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

    override fun saveAESCryptoKey(key: ByteArray) =
        keyValueStorage.update {
            it.putBytes("CRYPTO_KEY_AES", key)
        }

    override fun getAESCryptoKey(): ByteArray? =
        keyValueStorage.read {
            it.readBytes("CRYPTO_KEY_AES")
        }

    override fun savePasswordEntered(value: Boolean) =
        keyValueStorage.update {
            it.putBoolean("PASSWORD_ENTERED", value)
        }

    override fun getPasswordEntered(): Boolean? =
        keyValueStorage.read {
            it.readBoolean("PASSWORD_ENTERED")
        }

    override fun savePassword(value: String) =
        keyValueStorage.update {
            it.putString("PASSWORD", value)
        }

    override fun getPassword(): String? =
        keyValueStorage.read {
            it.readString("PASSWORD")
        }

    override fun savePasswordHint(value: String) =
        keyValueStorage.update {
            it.putString("PASSWORD_HINT", value)
        }

    override fun getPasswordHint(): String? =
        keyValueStorage.read {
            it.readString("PASSWORD_HINT")
        }
}