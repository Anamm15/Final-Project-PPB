package com.example.fitness.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.local.entity.RewardEntity
import com.example.fitness.data.repository.RedeemResult
import com.example.fitness.data.repository.RewardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RewardUiState(
    val rewards: List<RewardEntity> = emptyList(),
    val pointBalance: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val repository: RewardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Long = savedStateHandle.get<Long>("userId") ?: 0L

    val uiState: StateFlow<RewardUiState> =
        combine(
            repository.observeActiveRewards(),
            repository.observePointBalance(userId)
        ) { rewards, balance ->
            RewardUiState(rewards = rewards, pointBalance = balance, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RewardUiState()
        )

    // Event untuk Snackbar
    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    fun redeem(rewardId: Long) {
        viewModelScope.launch {
            val message = when (repository.redeem(userId, rewardId)) {
                RedeemResult.Success -> "Penukaran berhasil!"
                RedeemResult.InsufficientPoints -> "Poin kamu belum cukup"
                RedeemResult.OutOfStock -> "Stok reward habis"
                is RedeemResult.Error -> "Gagal menukar reward"
            }
            _messages.send(message)
        }
    }
}