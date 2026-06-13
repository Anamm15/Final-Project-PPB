package com.example.fitness.util

fun formatDuration(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun formatDurationLabel(totalSeconds: Long): String {
    return if (totalSeconds >= 60) {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        if (seconds == 0L) "$minutes menit" else "$minutes m $seconds detik"
    } else {
        "$totalSeconds detik"
    }
}