package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.ui.theme.md_theme_dark_primary
import com.example.passwordmanager.ui.theme.md_theme_dark_secondary

@Composable
fun CategoriesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(md_theme_dark_primary, md_theme_dark_secondary)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TODO: Search bar
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: Categories row
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: Passwords list by category
        }
    }
}
