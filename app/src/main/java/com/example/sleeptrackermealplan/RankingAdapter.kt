package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sleeptrackermealplan.data.UserRanking
import com.example.sleeptrackermealplan.databinding.ListItemRankingBinding

class RankingAdapter : ListAdapter<UserRanking, RankingAdapter.RankingViewHolder>(RankingDiffCallback()) {

    private var scoreUnit: String = "pts" // Default unit

    fun setScoreUnit(unit: String) {
        scoreUnit = unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ListItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RankingViewHolder(private val binding: ListItemRankingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userRanking: UserRanking) {
            binding.rankNumber.text = userRanking.rank.toString()
            binding.userName.text = userRanking.userName
            binding.userScore.text = "${userRanking.score} $scoreUnit" // Use the dynamic unit

            Glide.with(binding.userAvatar.context)
                .load(userRanking.avatarUrl)
                .circleCrop()
                .into(binding.userAvatar)
        }
    }
}

class RankingDiffCallback : DiffUtil.ItemCallback<UserRanking>() {
    override fun areItemsTheSame(oldItem: UserRanking, newItem: UserRanking): Boolean {
        return oldItem.rank == newItem.rank && oldItem.userName == newItem.userName
    }

    override fun areContentsTheSame(oldItem: UserRanking, newItem: UserRanking): Boolean {
        return oldItem == newItem
    }
}

