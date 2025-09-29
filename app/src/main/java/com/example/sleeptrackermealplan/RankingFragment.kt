package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeptrackermealplan.data.UserRanking
import com.example.sleeptrackermealplan.databinding.FragmentRankingBinding
import com.google.android.material.tabs.TabLayout

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabs()

        // Load initial data for the first tab
        updateRankingList(0)

        // Handle Back Button Click
        binding.toolbarRanking.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        rankingAdapter = RankingAdapter()
        binding.rankingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rankingAdapter
        }
    }

    private fun setupTabs() {
        binding.rankingTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    updateRankingList(it.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateRankingList(tabPosition: Int) {
        when (tabPosition) {
            0 -> { // Sleep Score
                rankingAdapter.setScoreUnit("pts")
                rankingAdapter.submitList(getSleepScoreData())
            }
            1 -> { // Meal Streak
                rankingAdapter.setScoreUnit("days")
                rankingAdapter.submitList(getMealStreakData())
            }
            2 -> { // Achievements
                rankingAdapter.setScoreUnit("achieved")
                rankingAdapter.submitList(getAchievementData())
            }
        }
    }

    // --- TODO: Replace with your actual data fetching logic ---
    private fun getSleepScoreData(): List<UserRanking> {
        return listOf(
            UserRanking(1, "Alvan", 92, R.drawable.palceholder),
            UserRanking(2, "Budi", 88, R.drawable.palceholder),
            UserRanking(3, "Cici", 85, R.drawable.palceholder),
            UserRanking(4, "David", 81, R.drawable.palceholder),
            UserRanking(5, "Eka", 79, R.drawable.palceholder)
        )
    }

    private fun getMealStreakData(): List<UserRanking> {
        return listOf(
            UserRanking(1, "Fina", 21, R.drawable.palceholder),
            UserRanking(2, "Gita", 18, R.drawable.palceholder),
            UserRanking(3, "Alvan", 15, R.drawable.palceholder),
            UserRanking(4, "Hadi", 12, R.drawable.palceholder),
            UserRanking(5, "Cici", 10, R.drawable.palceholder)
        )
    }

    private fun getAchievementData(): List<UserRanking> {
        return listOf(
            UserRanking(1, "David", 12, R.drawable.palceholder),
            UserRanking(2, "Alvan", 10, R.drawable.palceholder),
            UserRanking(3, "Budi", 9, R.drawable.palceholder),
            UserRanking(4, "Fina", 7, R.drawable.palceholder),
            UserRanking(5, "Eka", 5, R.drawable.palceholder)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

