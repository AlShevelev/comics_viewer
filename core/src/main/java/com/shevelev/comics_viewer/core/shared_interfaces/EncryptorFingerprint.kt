package com.shevelev.comics_viewer.core.shared_interfaces

import javax.crypto.Cipher

/**
 * Interface for Fingerprint authentication
 */
interface EncryptorFingerprint {
    /**
     * Get Cipher for ForFingerprintAuthentication
     */
    fun getCipher(): Cipher
}