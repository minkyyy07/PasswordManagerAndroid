package com.example.passwordmanager.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.theme.md_theme_dark_primary
import com.example.passwordmanager.ui.theme.md_theme_dark_secondary
import com.example.passwordmanager.ui.viewmodel.PasswordViewModel
import kotlin.math.roundToInt

private val categoryEmojis = mapOf(
    "Websites" to "🌐",
    "Applications" to "📱",
    "Social Media" to "💬",
    "Payments" to "💳",
    "Other" to "🔑",
    "All" to "⭐"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    passwordViewModel: PasswordViewModel,
    onAddClick: () -> Unit,
    onView: (Long) -> Unit = {},
    onEdit: (Long) -> Unit = {},
    onDelete: (Long) -> Unit = {}
) {
    val categories = passwordViewModel.categories
    val selectedCategory = passwordViewModel.selectedCategory
    var searchQuery by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val filteredPasswords = passwordViewModel.filteredPasswords.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.username.contains(searchQuery, ignoreCase = true)
    }

    val strongCount = filteredPasswords.count { isStrongPassword(it.password) }
    val securityScore = if (filteredPasswords.isNotEmpty()) strongCount * 100 / filteredPasswords.size else 0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = md_theme_dark_secondary) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF10192A)) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Visibility, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = md_theme_dark_secondary,
                        selectedTextColor = md_theme_dark_secondary
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Edit, contentDescription = "Categories") },
                    label = { Text("Categories") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Delete, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(md_theme_dark_primary, md_theme_dark_secondary)))
                .padding(padding)
        ) {
            // Шапка + security score
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(md_theme_dark_primary, md_theme_dark_secondary)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp).clip(CircleShape).shadow(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("🔒", fontSize = 32.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Welcome back!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Your vault is safe.",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                    // Индикатор security score
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                        CircularProgressIndicator(
                            progress = securityScore / 100f,
                            color = md_theme_dark_secondary,
                            strokeWidth = 7.dp,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "$securityScore%",
                                color = md_theme_dark_secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text("Secure", color = md_theme_dark_secondary, fontSize = 11.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Поиск
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Категории
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val emoji = categoryEmojis[category] ?: ""
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { passwordViewModel.selectCategory(category) },
                        label = { Text("$emoji $category") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = md_theme_dark_secondary.copy(alpha = 0.22f),
                            selectedLabelColor = md_theme_dark_secondary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        )
                    )
                }
            }
            // Список паролей с анимацией
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredPasswords, key = { it.id }) { entry ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .shadow(8.dp, RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Brush.horizontalGradient(
                                    listOf(
                                        md_theme_dark_primary.copy(alpha = 0.85f),
                                        md_theme_dark_secondary.copy(alpha = 0.80f)
                                    )
                                ).toColor(),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val emoji = categoryEmojis[entry.category] ?: "🔑"
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(md_theme_dark_secondary.copy(alpha = 0.22f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(emoji, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(entry.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(entry.username, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(entry.category, color = md_theme_dark_secondary, fontSize = 13.sp)
                                }
                                // Кнопки действий
                                Row {
                                    IconButton(onClick = { onView(entry.id) }) {
                                        Icon(Icons.Filled.Visibility, contentDescription = "View", tint = md_theme_dark_secondary)
                                    }
                                    IconButton(onClick = { onEdit(entry.id) }) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = md_theme_dark_secondary)
                                    }
                                    IconButton(onClick = { onDelete(entry.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 52.dp, bottom = 6.dp)
                            ) {
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(entry.password))
                                    // Можно показать Snackbar/Toast
                                }) {
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy Password", tint = md_theme_dark_primary)
                                }
                                Text("Copy password", color = md_theme_dark_primary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Для градиентного CardDefaults.cardColors
private fun Brush.toColor(): Color =
    Color(0xFF222A3A).copy(alpha = 0.95f)

fun isStrongPassword(password: String): Boolean {
    // Пример: длина >= 8, есть цифра, буква, спецсимвол
    return password.length >= 8 &&
            password.any { it.isDigit() } &&
            password.any { it.isLetter() } &&
            password.any { !it.isLetterOrDigit() }
}
