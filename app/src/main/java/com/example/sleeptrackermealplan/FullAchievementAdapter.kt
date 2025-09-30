package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptrackermealplan.data.AchievementDetail
import com.example.sleeptrackermealplan.databinding.ListItemFullAchievementBinding

class FullAchievementAdapter : ListAdapter<AchievementDetail, FullAchievementAdapter.AchievementViewHolder>(AchievementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ListItemFullAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = getItem(position)
        holder.bind(achievement)
    }

    class AchievementViewHolder(private val binding: ListItemFullAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: AchievementDetail) {
            binding.achievementIcon.setImageResource(achievement.iconResId)
            binding.achievementTitle.text = achievement.title
            binding.achievementDescription.text = achievement.description

            // Adjust opacity for locked vs. unlocked achievements
            val alpha = if (achievement.isUnlocked) 1.0f else 0.4f
            binding.root.alpha = alpha
        }
    }

    class AchievementDiffCallback : DiffUtil.ItemCallback<AchievementDetail>() {
        override fun areItemsTheSame(oldItem: AchievementDetail, newItem: AchievementDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AchievementDetail, newItem: AchievementDetail): Boolean {
            return oldItem == newItem
        }
    }
}
