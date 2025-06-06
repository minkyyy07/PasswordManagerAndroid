package com.example.passwordmanager.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("main", "Home", Icons.Filled.Home)
    object Search : BottomNavItem("search", "Search", Icons.Filled.Search)
    object Settings : BottomNavItem("settings", "Settings", Icons.Filled.Settings)
}

@Composable
fun BottomNavigationBar(currentRoute: String, onItemSelected: (String) -> Unit) {
    NavigationBar {
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Search,
            BottomNavItem.Settings
        ).forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemSelected(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
