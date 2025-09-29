package com.example.sleeptrackermealplan

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // We have two tabs: "For you" and "Following"

    override fun createFragment(position: Int): Fragment {
        // Return a new fragment instance for each tab
        return when (position) {
            0 -> ForYouFragment()
            1 -> FollowingFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
