package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.theme.md_theme_dark_primary
import com.example.passwordmanager.ui.theme.md_theme_dark_secondary
import com.example.passwordmanager.ui.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(passwordViewModel: PasswordViewModel, onAddClick: () -> Unit) {
    val categories = passwordViewModel.categories
    val selectedCategory = passwordViewModel.selectedCategory
    val passwords = passwordViewModel.filteredPasswords

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(md_theme_dark_primary, md_theme_dark_secondary)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: User avatar
            Surface(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {}
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Welcome back!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        // Категории
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { passwordViewModel.selectCategory(category) },
                    label = { Text(category) }
                )
            }
        }

        // Список паролей
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(passwords) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(entry.title, fontWeight = FontWeight.Bold)
                        Text(entry.username, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(entry.category, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}
