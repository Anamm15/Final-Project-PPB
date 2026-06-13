package com.example.fitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val selectedTierId: Long = 2L,      // default: Plus
    val isLoading: Boolean = false,
    val error: String? = null,
    val registeredUserId: Long? = null  // != null -> sukses, navigasi
) {
    val isValid: Boolean
        get() = name.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    val tiers = membershipTiers

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onTierSelect(id: Long) = _uiState.update { it.copy(selectedTierId = id) }

    fun register() {
        val s = _uiState.value
        if (!s.isValid || s.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            authRepository.register(s.name, s.email, s.password)
                .onSuccess { userId ->
                    _uiState.update { it.copy(isLoading = false, registeredUserId = userId) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun consumeSuccess() = _uiState.update { it.copy(registeredUserId = null) }
}