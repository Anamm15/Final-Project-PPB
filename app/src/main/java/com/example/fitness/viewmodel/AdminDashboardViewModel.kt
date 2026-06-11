package com.example.fitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.local.dao.PaymentWithUser
import com.example.fitness.data.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AdminStats(
    val totalMembers: Int = 0,
    val activeMemberships: Int = 0,
    val totalRevenue: Double = 0.0,
    val successfulPayments: Int = 0,
    val totalActivities: Int = 0,
    val totalRedemptions: Int = 0
)

data class AdminDashboardUiState(
    val stats: AdminStats = AdminStats(),
    val recentPayments: List<PaymentWithUser> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    repository: AdminRepository
) : ViewModel() {

    // Gabungkan 5 angka statistik jadi satu objek (combine maksimal 5 flow bertipe)
    private val statsFlow = combine(
        repository.observeTotalMembers(),
        repository.observeActiveMemberships(),
        repository.observeTotalRevenue(),
        repository.observeTotalActivities(),
        repository.observeTotalRedemptions()
    ) { members, active, revenue, activities, redemptions ->
        AdminStats(
            totalMembers = members,
            activeMemberships = active,
            totalRevenue = revenue,
            totalActivities = activities,
            totalRedemptions = redemptions
        )
    }

    val uiState: StateFlow<AdminDashboardUiState> =
        combine(
            statsFlow,
            repository.observeSuccessfulPaymentCount(),
            repository.observeRecentPayments()
        ) { stats, payCount, payments ->
            AdminDashboardUiState(
                stats = stats.copy(successfulPayments = payCount),
                recentPayments = payments,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AdminDashboardUiState()
        )
}