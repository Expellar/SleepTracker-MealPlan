package com.example.sleeptrackermealplan

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// This adapter provides the fragments for the ViewPager2 in the Community screen.
class CommunityPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // We have two tabs: "For You" and "Following"

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostListFragment.newInstance("for_you")
            1 -> PostListFragment.newInstance("following")
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

