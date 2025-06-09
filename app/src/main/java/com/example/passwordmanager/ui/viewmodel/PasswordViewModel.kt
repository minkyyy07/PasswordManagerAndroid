package com.example.passwordmanager.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.passwordmanager.model.PasswordEntry

class PasswordViewModel : ViewModel() {
    private val _passwordEntries = mutableStateListOf<PasswordEntry>()
    val passwords: SnapshotStateList<PasswordEntry> = _passwordEntries

    val categories = listOf("All", "Websites", "Applications", "Social Media", "Payments", "Other")

    var selectedCategory by mutableStateOf("All")

    val filteredPasswords: List<PasswordEntry>
        get() = if (selectedCategory == "All") passwords else passwords.filter { it.category == selectedCategory }

    fun addPassword(passwordEntry: PasswordEntry) {
        _passwordEntries.add(passwordEntry)
    }

    fun selectCategory(category: String) {
        selectedCategory = category
    }
}