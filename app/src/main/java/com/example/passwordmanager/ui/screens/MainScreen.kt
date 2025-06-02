package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.model.PasswordEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToDetail: (PasswordEntry) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Sample password entries for demonstration
    val passwordEntries = remember {
        mutableStateListOf(
            PasswordEntry(
                id = 1,
                title = "Google",
                username = "user@example.com",
                password = "StrongPassword123!",
                url = "https://google.com",
                notes = "My Google account"
            ),
            PasswordEntry(
                id = 2,
                title = "GitHub",
                username = "developer",
                password = "SecureCode456!",
                url = "https://github.com",
                notes = "Work GitHub account"
            ),
            PasswordEntry(
                id = 3,
                title = "Amazon",
                username = "shopper",
                password = "Shop789!",
                url = "https://amazon.com",
                notes = "Personal shopping account"
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Password Manager") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement settings */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Password")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search passwords...") },
                singleLine = true
            )
            
            // Password entries list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredEntries = passwordEntries.filter { 
                    it.title.contains(searchQuery, ignoreCase = true) || 
                    it.username.contains(searchQuery, ignoreCase = true) 
                }
                
                if (filteredEntries.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isEmpty()) 
                                    "No passwords saved yet. Add your first one!" 
                                else 
                                    "No passwords match your search.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredEntries) { entry ->
                        PasswordEntryCard(
                            entry = entry,
                            onClick = { onNavigateToDetail(entry) }
                        )
                    }
                }
            }
        }
        
        // Add dialog
        if (showAddDialog) {
            AddEntryDialog(
                onDismiss = { showAddDialog = false },
                onSave = { newEntry ->
                    passwordEntries.add(newEntry)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordEntryCard(entry: PasswordEntry, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = entry.username,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}