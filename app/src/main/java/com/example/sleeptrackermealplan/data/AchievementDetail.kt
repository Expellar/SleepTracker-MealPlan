package com.example.sleeptrackermealplan.data

/**
 * A data class to represent a single achievement with all its details.
 */
data class AchievementDetail(
    val id: String,
    val title: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean
)
