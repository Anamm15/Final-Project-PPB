package com.example.fitness.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitness.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoggedIn: (userId: Long, role: String) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.loggedInUserId) {
        state.loggedInUserId?.let { id ->
            onLoggedIn(id, state.loggedInRole ?: "USER")
            viewModel.consumeSuccess()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Masuk",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            "Selamat datang kembali.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let { msg ->
            Spacer(Modifier.height(8.dp))
            Text(
                msg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = viewModel::login,
            enabled = state.isValid && !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Masuk", fontWeight = FontWeight.Medium)
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Belum punya akun?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onRegisterClick) {
                Text("Daftar", fontWeight = FontWeight.Medium)
            }
        }
    }
}