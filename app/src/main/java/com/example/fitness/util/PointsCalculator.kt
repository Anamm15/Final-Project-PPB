package com.example.fitness.util

object PointsCalculator {
    private const val BASE_POINTS = 10

    fun calculate(type: ActivityType, durationSeconds: Long): Int {
        val minutes = (durationSeconds / 60).toInt()
        return BASE_POINTS + minutes * type.pointsPerMinute
    }
}