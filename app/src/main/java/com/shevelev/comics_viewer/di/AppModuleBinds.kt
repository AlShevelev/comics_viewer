package com.shevelev.comics_viewer.di

import com.shevelev.comics_viewer.core.di_scopes.ApplicationScope
import com.shevelev.comics_viewer.core.di_scopes.Clarification
import com.shevelev.comics_viewer.core.shared_interfaces.Encryptor
import com.shevelev.comics_viewer.core.shared_interfaces.EncryptorFingerprint
import com.shevelev.comics_viewer.core.shared_interfaces.KeyValueStorageFacade
import com.shevelev.comics_viewer.encryption.aes.EncryptorAES
import com.shevelev.comics_viewer.encryption.rsa.EncryptorRSA
import com.shevelev.comics_viewer.key_value_storage.KeyValueStorageFacadeImpl
import com.shevelev.comics_viewer.key_value_storage.storages.Storage
import com.shevelev.comics_viewer.key_value_storage.storages.StorageOperationsInstance
import com.shevelev.comics_viewer.key_value_storage.storages.combined.CombinedStorage
import com.shevelev.comics_viewer.key_value_storage.storages.in_memory.InMemoryStorage
import com.shevelev.comics_viewer.key_value_storage.storages.shared_preferences.SharedPreferencesStorage
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AppModuleBinds {
    //region Key-value storage
    @Binds
    abstract fun provideKeyValueStorageFacade(facade: KeyValueStorageFacadeImpl): KeyValueStorageFacade

    @Binds
    abstract fun provideKeyValueStorage(storage: CombinedStorage): Storage

    @Binds
    @ApplicationScope
    @Named(Clarification.CACHE)
    abstract fun provideCacheStorage(storage: InMemoryStorage): StorageOperationsInstance

    @Binds
    @Named(Clarification.PERSISTENT)
    abstract fun providePersistentStorage(storage: SharedPreferencesStorage): StorageOperationsInstance
    // endregion

    // region Encryption
    @Binds
    @Named(Clarification.RSA)
    abstract fun provideEncryptor(encryptor: EncryptorRSA): Encryptor

    @Binds
    abstract fun provideEncryptorForFingerprint(encryptor: EncryptorAES): EncryptorFingerprint
    // endregion
}
