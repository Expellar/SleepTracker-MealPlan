package com.example.sleeptrackermealplan

// Add a unique ID, store timestamp as a Long, and add isLiked status
data class Post(
    val id: String,
    val userName: String,
    val userAvatarUrl: String?,
    val timestamp: Long, // Changed from String to Long
    val postContent: String,
    val imageUrl: String?,
    var isLiked: Boolean = false // Added to track like status
)

