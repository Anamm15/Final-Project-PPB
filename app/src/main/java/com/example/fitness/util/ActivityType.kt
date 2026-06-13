package com.example.fitness.util

enum class ActivityType(val label: String, val pointsPerMinute: Int) {
    WEIGHT("Latihan beban", 3),
    CARDIO("Cardio", 2),
    YOGA("Yoga", 2),
    HIIT("HIIT", 4)
}