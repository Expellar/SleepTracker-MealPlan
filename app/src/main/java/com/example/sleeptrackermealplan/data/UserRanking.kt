package com.example.sleeptrackermealplan.data

data class UserRanking(
    val rank: Int,
    val userName: String,
    // This can represent sleep score, streak days, achievement count, etc.
    val score: Int,
    val avatarUrl: Int // Using Int for drawable resource ID for now
)

