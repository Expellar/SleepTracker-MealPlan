package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

// The adapter now accepts a function to handle like button clicks
class PostAdapter(private val onLikeClicked: (String) -> Unit) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val postTimestamp: TextView = itemView.findViewById(R.id.post_timestamp)
        val postContent: TextView = itemView.findViewById(R.id.post_content)
        val postImage: ImageView = itemView.findViewById(R.id.post_image)
        val likeButton: ImageButton = itemView.findViewById(R.id.button_like) // Get the like button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        holder.userName.text = post.userName
        // Convert the Long timestamp to a readable string
        holder.postTimestamp.text = getFormattedTimestamp(post.timestamp)
        holder.postContent.text = post.postContent

        Glide.with(holder.itemView.context).load(post.userAvatarUrl).circleCrop().into(holder.userAvatar)

        if (post.imageUrl != null) {
            holder.postImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.postImage)
        } else {
            holder.postImage.visibility = View.GONE
        }

        // --- LIKE BUTTON LOGIC ---
        // Set the icon based on whether the post is liked
        val likeIcon = if (post.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outline
        holder.likeButton.setImageResource(likeIcon)

        // Set the click listener to call the function passed to the adapter
        holder.likeButton.setOnClickListener {
            onLikeClicked(post.id)
        }
    }

    // Helper function to format the timestamp
    private fun getFormattedTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds < 60) return "Just now"
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        if (minutes < 60) return "$minutes min ago"
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        if (hours < 24) return "$hours hr ago"
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        return "$days days ago"
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

