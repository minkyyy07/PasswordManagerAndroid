package com.example.passwordmanager.data

data class PasswordEntry (
    val service: String,
    val username: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val category: String = "General",
    val notes: String = ""
)