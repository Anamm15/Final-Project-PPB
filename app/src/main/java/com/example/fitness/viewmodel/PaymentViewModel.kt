package com.example.fitness.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PaymentMethod(val label: String) {
    TRANSFER("Transfer bank"),
    EWALLET("E-wallet"),
    CARD("Kartu kredit/debit")
}

data class PaymentUiState(
    val selectedMethod: PaymentMethod = PaymentMethod.TRANSFER,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val paidMembershipId: Long? = null
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: Long = savedStateHandle.get<Long>("userId") ?: 0L
    private val tierId: Long = savedStateHandle.get<Long>("tierId") ?: 0L

    val tier: MembershipTier = membershipTiers.first { it.id == tierId }

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun onMethodSelect(method: PaymentMethod) =
        _uiState.update { it.copy(selectedMethod = method) }

    fun pay() {
        if (_uiState.value.isProcessing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            delay(1500)   // simulasi proses pembayaran

            paymentRepository.pay(userId, tier, _uiState.value.selectedMethod.name)
                .onSuccess { mId ->
                    _uiState.update { it.copy(isProcessing = false, paidMembershipId = mId) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isProcessing = false, error = e.message) }
                }
        }
    }

    fun consumeSuccess() = _uiState.update { it.copy(paidMembershipId = null) }
}