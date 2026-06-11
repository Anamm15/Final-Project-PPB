package com.example.fitness.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.data.repository.ActivityRepository
import com.example.fitness.util.ActivityType
import com.example.fitness.util.PointsCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SessionPhase { IDLE, RUNNING, FINISHED }

data class ActivityUiState(
    val phase: SessionPhase = SessionPhase.IDLE,
    val selectedType: ActivityType = ActivityType.WEIGHT,
    val elapsedSeconds: Long = 0,
    val earnedPoints: Int = 0
) {
    val livePoints: Int get() = PointsCalculator.calculate(selectedType, elapsedSeconds)
}

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Long = savedStateHandle.get<Long>("userId") ?: 0L

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    val activityTypes: List<ActivityType> = ActivityType.entries

    private var timerJob: Job? = null
    private var startTime: Long = 0L

    fun onSelectType(type: ActivityType) {
        if (_uiState.value.phase == SessionPhase.IDLE) {
            _uiState.update { it.copy(selectedType = type) }
        }
    }

    fun start() {
        if (_uiState.value.phase != SessionPhase.IDLE) return
        startTime = System.currentTimeMillis()
        _uiState.update { it.copy(phase = SessionPhase.RUNNING, elapsedSeconds = 0) }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun finish() {
        if (_uiState.value.phase != SessionPhase.RUNNING) return
        timerJob?.cancel()

        val current = _uiState.value
        val endTime = System.currentTimeMillis()
        val points = PointsCalculator.calculate(current.selectedType, current.elapsedSeconds)

        viewModelScope.launch {
            repository.logActivity(
                userId = userId,
                type = current.selectedType.label,
                startTime = startTime,
                endTime = endTime,
                points = points
            )
            _uiState.update { it.copy(phase = SessionPhase.FINISHED, earnedPoints = points) }
        }
    }

    fun cancel() {
        timerJob?.cancel()
        _uiState.update { ActivityUiState(selectedType = it.selectedType) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}