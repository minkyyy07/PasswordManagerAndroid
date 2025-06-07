package com.example.passwordmanager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import com.example.passwordmanager.ui.screens.HomeScreen
import com.example.passwordmanager.ui.screens.PasswordManagerNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            setContent {
                PasswordManagerTheme {
                    // Здесь должен вызываться HomeScreen, WelcomeScreen или CategoriesScreen
                    PasswordManagerNavGraph()
                }
            }
        }, 1500) // 1.5 seconds delay
    }
}