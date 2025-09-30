package com.example.sleeptrackermealplan.data

/**
 * A data class to hold the calculated statistics for the progress screen.
 */
data class ProgressStats(
    val avgSleepScore: Int,
    val avgSleepHours: Double,
    val goalStreak: Int
)

