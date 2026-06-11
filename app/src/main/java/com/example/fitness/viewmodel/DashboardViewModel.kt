package com.example.fitness.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.local.entity.ActivityEntity
import com.example.fitness.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "",
    val tierName: String? = null,
    val activeUntil: Long? = null,
    val pointBalance: Int = 0,
    val recentActivities: List<ActivityEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: DashboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: Long = savedStateHandle.get<Long>("userId") ?: 0L

    val uiState: StateFlow<DashboardUiState> =
        combine(
            repository.observeUser(userId),
            repository.observeActiveMembership(userId),
            repository.observePointBalance(userId),
            repository.observeRecentActivities(userId)
        ) { user, membership, balance, activities ->
            DashboardUiState(
                userName = user?.name ?: "",
                tierName = membership?.let { m ->
                    membershipTiers.firstOrNull { it.id == m.packageId }?.name
                },
                activeUntil = membership?.endDate,
                pointBalance = balance,
                recentActivities = activities.take(5),
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}