package com.shevelev.comics_viewer.key_value_storage.storages.shared_preferences

import android.content.Context
import android.util.Base64
import com.shevelev.comics_viewer.key_value_storage.storages.StorageReadOperations
import com.shevelev.comics_viewer.utils.extentions.fromBase64ToBytes

class SharedPreferencesStorageReadOperations(context: Context, name: String): StorageReadOperations {
    private val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    /** Check is item exists  */
    override fun contains(key: String): Boolean = preferences.contains(key)

    /** Read boolean value  */
    override fun readBoolean(key: String): Boolean? = read(key) { preferences.getBoolean(key, false)}

    /** Read string value  */
    override fun readString(key: String): String? = read(key) { preferences.getString(key, "")}

    /** Read float value  */
    override fun readFloat(key: String): Float? = read(key) { preferences.getFloat(key, 0F)}

    /** Read int value  */
    override fun readInt(key: String): Int? = read(key) { preferences.getInt(key, 0)}

    /** Read long value  */
    override fun readLong(key: String): Long? = read(key) { preferences.getLong(key, 0L)}

    /** Read long value  */
    override fun readBytes(key: String): ByteArray? = read(key)  { preferences.getString(key, null)?.fromBase64ToBytes() }

    private fun <T>read(key: String, readAction: () -> T): T? =
        if(preferences.contains(key)) readAction() else null
}