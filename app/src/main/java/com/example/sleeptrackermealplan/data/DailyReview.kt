package com.example.sleeptrackermealplan.data

import java.util.Calendar

/**
 * Represents the review status for a single day.
 * This version has the correct properties.
 */
data class DailyReview(
    val calendar: Calendar,
    val status: ReviewStatus,
    var isSelected: Boolean = false
)

enum class ReviewStatus {
    GOOD, OKAY, BAD
}

