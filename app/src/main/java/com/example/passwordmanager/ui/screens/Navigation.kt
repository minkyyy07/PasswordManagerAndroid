package com.example.passwordmanager.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.model.PasswordEntry
import com.example.passwordmanager.ui.viewmodel.PasswordViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Add : Screen("add")
    object Categories : Screen("categories")
    object Detail : Screen("detail")
    object Settings : Screen("settings")
}

@Composable
fun PasswordManagerNavGraph(navController: NavHostController = rememberNavController()) {
    val passwordViewModel: PasswordViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onGetStarted = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                passwordViewModel = passwordViewModel,
                onAddClick = { navController.navigate(Screen.Add.route) }
            )
        }
        composable(Screen.Add.route) {
            AddPasswordScreen(
                onSave = { title, username, password, url, notes, category ->
                    passwordViewModel.addPassword(
                        PasswordEntry(
                            id = System.currentTimeMillis(),
                            title = title,
                            username = username,
                            password = password,
                            url = url,
                            notes = notes,
                            category = category
                        )
                    )
                    navController.popBackStack(Screen.Home.route, false)
                },
                onCancel = { navController.popBackStack() }
            )
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