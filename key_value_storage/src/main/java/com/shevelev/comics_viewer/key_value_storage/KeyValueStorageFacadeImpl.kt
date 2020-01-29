package com.shevelev.comics_viewer.key_value_storage

import com.shevelev.comics_viewer.core.di_scopes.Clarification
import com.shevelev.comics_viewer.core.shared_interfaces.Encryptor
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.key_value_storage.storages.Storage
import com.shevelev.comics_viewer.utils.extentions.fromBytes
import com.shevelev.comics_viewer.utils.extentions.toBytes
import javax.inject.Inject
import javax.inject.Named

/**
 * Helper class for access to App-level private shared preferences
 */
class KeyValueStorageFacadeImpl
@Inject
constructor(
    @Named(Clarification.COMBINED)
    private val keyValueCombinedStorage: Storage,
    @Named(Clarification.IN_MEMORY)
    private val keyValueInMemoryStorage: Storage,
    private val encryptor: Encryptor
) : KeyValueStorageFacade {
     override fun savePasswordEntered(value: Boolean) =
        keyValueInMemoryStorage.update {
            it.putBoolean("PASSWORD_ENTERED", value)
        }

    override fun getPasswordEntered(): Boolean? =
        keyValueInMemoryStorage.read {
            it.readBoolean("PASSWORD_ENTERED")
        }

    override fun savePassword(value: String) =
        keyValueCombinedStorage.update {
            it.putBytes("PASSWORD", encryptor.encrypt(value.toBytes())!!)
        }

    override fun getPassword(): String? =
        keyValueCombinedStorage.read {
            encryptor.decrypt(it.readBytes("PASSWORD"))?.fromBytes()
        }

    override fun savePasswordHint(value: String) =
        keyValueCombinedStorage.update {
            it.putString("PASSWORD_HINT", value)
        }

    override fun getPasswordHint(): String? =
        keyValueCombinedStorage.read {
            it.readString("PASSWORD_HINT")
        }
}