package com.example.fitness.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.local.entity.ActivityEntity
import com.example.fitness.data.local.entity.MembershipEntity
import com.example.fitness.data.local.entity.UserEntity
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
    val dailyStreak: Int = 0,
    val hasActivityToday: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: DashboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Long =
        savedStateHandle.get<Long>("userId") ?: 0L

    val uiState: StateFlow<DashboardUiState> =
        combine(
            repository.observeUser(userId),
            repository.observeActiveMembership(userId),
            repository.observePointBalance(userId),
            repository.observeRecentActivities(userId),
            repository.observeDailyStreak(userId),
            repository.observeHasActivityToday(userId)
        ) { values ->

            val user = values[0] as UserEntity?
            val membership = values[1] as MembershipEntity?
            val balance = values[2] as Int
            val activities = values[3] as List<ActivityEntity>
            val streak = values[4] as Int
            val hasActivityToday = values[5] as Boolean

            DashboardUiState(
                userName = user?.name ?: "",
                tierName = null,
                activeUntil = membership?.endDate,
                pointBalance = balance,
                recentActivities = activities.take(5),
                dailyStreak = streak,
                hasActivityToday = hasActivityToday,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}