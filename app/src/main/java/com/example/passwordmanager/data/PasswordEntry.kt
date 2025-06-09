package com.example.passwordmanager.data

data class PasswordEntry(
    val id: Long,
    val title: String,
    val username: String,
    val password: String,
    val category: String = "Другое", // <--- добавь это поле
    val url: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)