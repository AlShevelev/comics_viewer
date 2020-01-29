package com.shevelev.comics_viewer.utils.extentions

import android.util.Base64

fun String.toBytes(): ByteArray = this.toByteArray(charset("UTF-8"))
fun ByteArray.fromBytes(): String = String(this, charset("UTF-8"))

fun String.fromBase64ToBytes(): ByteArray = Base64.decode(this, Base64.DEFAULT)
fun ByteArray.fromBytesToBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)


