package com.example.sleeptrackermealplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID

class PostViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>() // Use immutable List
    val posts: LiveData<List<Post>> = _posts

    init {
        val initialPosts = listOf(
            Post(
                id = UUID.randomUUID().toString(),
                userName = "Carlo",
                userAvatarUrl = "https://placehold.co/100x100/EFEFEF/AAAAAA&text=A",
                timestamp = System.currentTimeMillis() - 7200000,
                postContent = "Welcome to the community! This is the first post.",
                imageUrl = null,
                isLiked = true
            ),
            Post(
                id = UUID.randomUUID().toString(),
                userName = "Jane Doe",
                userAvatarUrl = "https://placehold.co/100x100/EFEFEF/AAAAAA&text=J",
                timestamp = System.currentTimeMillis() - 86400000,
                postContent = "Just tried a new healthy recipe, it was delicious.",
                imageUrl = "https://placehold.co/600x400/CCCCCC/AAAAAA&text=Food+Image",
                isLiked = false
            )
        )
        _posts.value = initialPosts
    }

    fun addPost(post: Post) {
        val currentPosts = _posts.value ?: emptyList()
        // Add the new post to a new list
        _posts.value = listOf(post) + currentPosts
    }

    /**
     * Toggles the like status by creating a new list with the updated post.
     * This is the corrected, immutable approach.
     */
    fun toggleLikeStatus(postId: String) {
        val currentPosts = _posts.value ?: return

        // Create a new list with the updated item
        val updatedPosts = currentPosts.map { post ->
            if (post.id == postId) {
                // Use copy() to create a new Post object with the isLiked value flipped
                post.copy(isLiked = !post.isLiked)
            } else {
                post
            }
        }
        _posts.value = updatedPosts
    }
}

