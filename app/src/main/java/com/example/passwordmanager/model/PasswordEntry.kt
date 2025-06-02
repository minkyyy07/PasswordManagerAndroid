package com.example.passwordmanager.model

/**
 * Data class representing a password entry in the password manager
 * @param id Unique identifier for the entry
 * @param title Title or name of the entry (e.g., website or service name)
 * @param username Username or email associated with the entry
 * @param password Password for the entry
 * @param url URL of the website or service (optional)
 * @param notes Additional notes about the entry (optional)
 * @param createdAt Timestamp when the entry was created
 * @param updatedAt Timestamp when the entry was last updated
 */
data class PasswordEntry(
    val id: Long,
    val title: String,
    val username: String,
    val password: String,
    val url: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
