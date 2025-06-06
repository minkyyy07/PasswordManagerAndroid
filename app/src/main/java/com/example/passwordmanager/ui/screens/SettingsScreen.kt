package com.example.passwordmanager.ui.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val versionName = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: "1.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0"
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            headlineContent = { Text("Profile", fontWeight = FontWeight.Bold) },
            supportingContent = { Text("Manage your profile information") },
            modifier = Modifier.clickable { /* TODO: Navigate to profile */ }
        )
        Divider()
        ListItem(
            headlineContent = { Text("Permissions", fontWeight = FontWeight.Bold) },
            supportingContent = { Text("App permissions and security settings") },
            modifier = Modifier.clickable { /* TODO: Navigate to permissions */ }
        )
        Divider()
        ListItem(
            headlineContent = { Text("About", fontWeight = FontWeight.Bold) },
            supportingContent = { Text("Information about the app") },
            modifier = Modifier.clickable { /* TODO: Show about dialog */ }
        )
        Divider()
        ListItem(
            headlineContent = { Text("Help", fontWeight = FontWeight.Bold) },
            supportingContent = { Text("Get help and support") },
            modifier = Modifier.clickable { /* TODO: Show help */ }
        )
        Divider()
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "App version: $versionName",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
