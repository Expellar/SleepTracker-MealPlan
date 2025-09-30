package com.example.sleeptrackermealplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptrackermealplan.data.Achievement
import com.example.sleeptrackermealplan.data.DailyReview
import com.example.sleeptrackermealplan.data.ProgressStats
import com.example.sleeptrackermealplan.data.ReviewStatus
import java.util.Calendar

class ProgressViewModel : ViewModel() {

    // LiveData for the main statistics
    private val _progressStats = MutableLiveData<ProgressStats>()
    val progressStats: LiveData<ProgressStats> = _progressStats

    // LiveData for the list of achievements
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements

    // --- THIS WAS MISSING ---
    // LiveData for the weekly review data
    private val _weeklyReview = MutableLiveData<List<DailyReview>>()
    val weeklyReview: LiveData<List<DailyReview>> = _weeklyReview
    // --- END OF MISSING PART ---

    init {
        updatePeriod("1W")
        loadAchievements()
        loadWeeklyReview() // Load initial weekly data
    }

    fun updatePeriod(period: String) {
        _progressStats.value = when (period) {
            "1W" -> ProgressStats(avgSleepScore = 82, avgSleepHours = 7.8, goalStreak = 12)
            "1M" -> ProgressStats(avgSleepScore = 78, avgSleepHours = 7.5, goalStreak = 25)
            "6M" -> ProgressStats(avgSleepScore = 80, avgSleepHours = 7.6, goalStreak = 45)
            "1Y" -> ProgressStats(avgSleepScore = 75, avgSleepHours = 7.2, goalStreak = 90)
            "All" -> ProgressStats(avgSleepScore = 77, avgSleepHours = 7.4, goalStreak = 150)
            else -> ProgressStats(0, 0.0, 0)
        }
    }

    private fun loadAchievements() {
        _achievements.value = listOf(
            Achievement(id = "1", iconResId = R.drawable.ic_achievement_moon, isUnlocked = true),
            Achievement(id = "2", iconResId = R.drawable.ic_achievement_fork_knife, isUnlocked = true),
            Achievement(id = "3", iconResId = R.drawable.ic_achievement_calendar_tick, isUnlocked = false)
            // Add more achievements as needed
        )
    }

    // This function simulates loading the weekly data
    private fun loadWeeklyReview() {
        val reviews = mutableListOf<DailyReview>()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Start 6 days ago
        for (i in 0..6) {
            reviews.add(DailyReview(calendar.clone() as Calendar, getRandomReviewStatus()))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        _weeklyReview.value = reviews
    }

    // Helper function to get a random status for dummy data
    fun getRandomReviewStatus(): ReviewStatus {
        return ReviewStatus.values().random()
    }
}

