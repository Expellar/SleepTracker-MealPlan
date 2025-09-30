package com.example.sleeptrackermealplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptrackermealplan.data.AchievementDetail

class AchievementsViewModel : ViewModel() {

    private val _achievements = MutableLiveData<List<AchievementDetail>>()
    val achievements: LiveData<List<AchievementDetail>> = _achievements

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        // In a real app, you'd load this from a database and check against user progress.
        // For now, we'll use a dummy list with a sleep and meal theme.
        _achievements.value = listOf(
            AchievementDetail("early_bird", "Early Bird", "Woke up before 7 AM for 3 days", R.drawable.ic_achievement_moon, true),
            AchievementDetail("night_owl", "Night Owl", "Got 8+ hours of sleep", R.drawable.ic_achievement_moon, true),
            AchievementDetail("sleep_master", "Sleep Master", "7 consecutive days of 8+ hours sleep", R.drawable.ic_achievement_moon, false),

            AchievementDetail("healthy_start", "Healthy Start", "Logged a healthy breakfast", R.drawable.ic_achievement_fork_knife, true),
            AchievementDetail("meal_planner", "Meal Planner", "Planned meals for a full week", R.drawable.ic_achievement_fork_knife, true),
            AchievementDetail("nutrition_pro", "Nutrition Pro", "Logged 50 healthy meals", R.drawable.ic_achievement_fork_knife, false),

            AchievementDetail("one_week_streak", "One Week Streak", "Logged in for 7 days in a row", R.drawable.ic_achievement_calendar_tick, true),
            AchievementDetail("perfect_month", "Perfect Month", "Logged in every day for a month", R.drawable.ic_achievement_calendar_tick, false),
            AchievementDetail("year_of_health", "Year of Health", "Logged in for 365 days", R.drawable.ic_achievement_calendar_tick, false)
        )
    }
}
