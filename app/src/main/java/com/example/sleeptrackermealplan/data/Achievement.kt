package com.example.sleeptrackermealplan.data

/**
 * A data class to represent a single achievement.
 * @param id A unique identifier for the achievement.
 * @param iconResId The drawable resource ID for the achievement's icon.
 * @param isUnlocked Whether the user has earned this achievement.
 */
data class Achievement(
    val id: String,
    val iconResId: Int,
    val isUnlocked: Boolean
)

