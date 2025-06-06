package com.example.passwordmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.theme.*
import com.example.passwordmanager.util.PasswordGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    onNavigateBack: () -> Unit,
    onSavePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordLength by remember { mutableStateOf(12) }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSpecialChars by remember { mutableStateOf(true) }

    var generatedPassword by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        generatedPassword = PasswordGenerator.generate(
            length = passwordLength,
            includeUppercase = includeUppercase,
            includeLowercase = includeLowercase,
            includeNumbers = includeNumbers,
            includeSpecialChars = includeSpecialChars
        )
    }

    Scaffold(
         topBar = {

         }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

        }
    }
}

