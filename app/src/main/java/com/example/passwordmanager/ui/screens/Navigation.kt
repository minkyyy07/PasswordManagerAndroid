package com.example.passwordmanager.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.ui.screens.WelcomeScreen
import com.example.passwordmanager.ui.screens.HomeScreen
import com.example.passwordmanager.ui.screens.CategoriesScreen
import com.example.passwordmanager.ui.screens.DetailScreen
import com.example.passwordmanager.ui.screens.SettingsScreen
import com.example.passwordmanager.model.PasswordEntry

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Categories : Screen("categories")
    object Detail : Screen("detail")
    object Settings : Screen("settings")
}

@Composable
fun PasswordManagerNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onGetStarted = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Categories.route) {
            CategoriesScreen()
        }
        composable(Screen.Detail.route) {
            val mockEntry = PasswordEntry(
                id = 0,
                title = "Example",
                username = "user@example.com",
                password = "password123",
                url = "https://example.com",
                notes = "",
                createdAt = 0L,
                updatedAt = 0L
            )
            DetailScreen(
                passwordEntry = mockEntry,
                onNavigateBack = {},
                onEdit = {},
                onDelete = {}
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
