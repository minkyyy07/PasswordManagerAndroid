package com.example.passwordmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.passwordmanager.model.PasswordEntry
import com.example.passwordmanager.ui.screens.DetailScreen
import com.example.passwordmanager.ui.screens.MainScreen
import com.google.gson.Gson

@Composable
fun PasswordManagerApp() {
    val navController = rememberNavController()
    
    // Shared ViewModel would be better in a real app, but for simplicity
    // we'll use a simple approach with Gson serialization for navigation
    val gson = remember { Gson() }
    
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToDetail = { passwordEntry ->
                    // Convert the password entry to a JSON string for navigation
                    val passwordJson = gson.toJson(passwordEntry)
                    navController.navigate("detail/$passwordJson")
                }
            )
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