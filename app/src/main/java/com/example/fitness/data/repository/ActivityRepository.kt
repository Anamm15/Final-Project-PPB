package com.example.fitness.data.repository

import com.example.fitness.data.local.dao.ActivityDao
import com.example.fitness.data.local.entity.ActivityEntity
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao
) {
    suspend fun logActivity(
        userId: Long,
        type: String,
        startTime: Long,
        endTime: Long,
        points: Int
    ) {
        activityDao.insert(
            ActivityEntity(
                userId = userId,
                type = type,
                startTime = startTime,
                endTime = endTime,
                pointsEarned = points,
                createdAt = endTime
            )
        )
    }
}