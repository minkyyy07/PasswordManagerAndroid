package com.example.passwordmanager.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Cryptographic utility class for secure operations
 */
object CryptoUtils {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val AES_CBC_TRANSFORMATION = "AES/CBC/PKCS7Padding"
    private const val KEY_ALIAS = "PasswordManagerKey"
    private const val GCM_TAG_LENGTH = 128
    private const val IV_LENGTH = 12

    /**
     * Generate a random initialization vector (IV)
     * @param length Length of IV in bytes
     * @return Random IV as byte array
     */
    fun generateIV(length: Int = IV_LENGTH): ByteArray {
        val iv = ByteArray(length)
        SecureRandom().nextBytes(iv)
        return iv
    }

    /**
     * Create or get the secret key from Android KeyStore
     * @return SecretKey for encryption/decryption
     */
    fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
                
            keyGenerator.init(keyGenParameterSpec)
            return keyGenerator.generateKey()
        }
        
        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    /**
     * Encrypt data using AES-GCM with Android KeyStore
     * @param plaintext Data to encrypt
     * @return Encrypted data with IV as Base64 string
     */
    fun encryptWithKeyStore(plaintext: String): String {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        val iv = generateIV()
        
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
        
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
        
        // Combine IV and encrypted data
        val combined = ByteArray(iv.size + encryptedBytes.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encryptedBytes, 0, combined, iv.size, encryptedBytes.size)
        
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    /**
     * Decrypt data using AES-GCM with Android KeyStore
     * @param encryptedData Encrypted data with IV as Base64 string
     * @return Decrypted data as string
     */
    fun decryptWithKeyStore(encryptedData: String): String {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        
        // Decode and extract IV and encrypted data
        val combined = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, IV_LENGTH)
        val encrypted = combined.copyOfRange(IV_LENGTH, combined.size)
        
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
        
        val decryptedBytes = cipher.doFinal(encrypted)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    /**
     * Encrypt data using AES-CBC with a provided key
     * @param plaintext Data to encrypt
     * @param key Encryption key
     * @return Pair of IV and encrypted data as Base64 strings
     */
    fun encryptWithPassword(plaintext: String, password: String): Pair<String, String> {
        val key = deriveKeyFromPassword(password)
        val iv = generateIV(16)  // CBC mode uses 16 bytes IV
        
        val cipher = Cipher.getInstance(AES_CBC_TRANSFORMATION)
        val ivSpec = IvParameterSpec(iv)
        val secretKeySpec = SecretKeySpec(key, KeyProperties.KEY_ALGORITHM_AES)
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        val encryptedBytes = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
        
        return Pair(
            Base64.encodeToString(iv, Base64.DEFAULT),
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        )
    }

    /**
     * Decrypt data using AES-CBC with a provided key
     * @param encryptedData Encrypted data as Base64 string
     * @param ivString IV as Base64 string
     * @param password Password to derive key from
     * @return Decrypted data as string
     */
    fun decryptWithPassword(encryptedData: String, ivString: String, password: String): String {
        val key = deriveKeyFromPassword(password)
        val iv = Base64.decode(ivString, Base64.DEFAULT)
        val encrypted = Base64.decode(encryptedData, Base64.DEFAULT)
        
        val cipher = Cipher.getInstance(AES_CBC_TRANSFORMATION)
        val ivSpec = IvParameterSpec(iv)
        val secretKeySpec = SecretKeySpec(key, KeyProperties.KEY_ALGORITHM_AES)
        
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        val decryptedBytes = cipher.doFinal(encrypted)
        
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    /**
     * Generate a hash of the password using SHA-256
     * @param password Password to hash
     * @return Hashed password as Base64 string
     */
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(hash, Base64.DEFAULT)
    }

    /**
     * Derive an encryption key from a password using PBKDF2 (simplified)
     * @param password Password to derive key from
     * @return Key as byte array
     */
    private fun deriveKeyFromPassword(password: String): ByteArray {
        // In a real app, you'd use a proper key derivation function like PBKDF2
        // This is a simplified version for demonstration
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(password.toByteArray(StandardCharsets.UTF_8))
    }

    /**
     * Generate a secure random string
     * @param length Length of the string
     * @return Random string
     */
    fun generateRandomString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?/"
        val random = SecureRandom()
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    /**
     * Check if two strings are equal in a time-constant manner
     * Helps prevent timing attacks when comparing passwords or hashes
     * @param a First string
     * @param b Second string
     * @return True if strings are equal
     */
    fun constantTimeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        
        return result == 0
    }
}
