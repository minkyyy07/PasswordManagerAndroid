package com.example.passwordmanager.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.passwordmanager.model.PasswordEntry

class PasswordViewModel : ViewModel() {
    private val _passwordEntries = mutableStateListOf<PasswordEntry>()
    val passwords: SnapshotStateList<PasswordEntry> = _passwordEntries

    fun addPassword(passwordEntry: PasswordEntry) {
        _passwordEntries.add(passwordEntry)
    }
}