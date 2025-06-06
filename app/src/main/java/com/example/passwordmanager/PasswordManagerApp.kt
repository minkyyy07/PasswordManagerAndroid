package com.example.passwordmanager

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.passwordmanager.model.PasswordEntry
import com.example.passwordmanager.ui.components.BottomNavigationBar
import com.example.passwordmanager.ui.screens.DetailScreen
import com.example.passwordmanager.ui.screens.MainScreen
import com.example.passwordmanager.ui.screens.SearchScreen
import com.example.passwordmanager.ui.screens.SettingsScreen
import com.google.gson.Gson
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

@Composable
fun PasswordManagerApp() {
    val navController = rememberNavController()
    val gson = remember { Gson() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/") ?: "main"

    Scaffold(
        bottomBar = {
            if (currentRoute == "main" || currentRoute == "search" || currentRoute == "settings") {
                BottomNavigationBar(currentRoute = currentRoute) { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("main") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") {
                MainScreen(
                    onNavigateToDetail = { passwordEntry ->
                        // Convert the password entry to a JSON string for navigation
                        val passwordJson = gson.toJson(passwordEntry)
                        navController.navigate("detail/$passwordJson")
                    }
                )
            }
            composable("search") {
                SearchScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
            composable(
                route = "detail/{passwordJson}",
                arguments = listOf(
                    navArgument("passwordJson") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // Extract and deserialize the password entry from the navigation argument
                val passwordJson = backStackEntry.arguments?.getString("passwordJson") ?: ""
                val passwordEntry = gson.fromJson(passwordJson, PasswordEntry::class.java)

                DetailScreen(
                    passwordEntry = passwordEntry,
                    onNavigateBack = { navController.popBackStack() },
                    onEdit = { /* TODO: Implement edit functionality */ },
                    onDelete = { /* TODO: Implement delete functionality */ }
                )
            }
        }
    }
}