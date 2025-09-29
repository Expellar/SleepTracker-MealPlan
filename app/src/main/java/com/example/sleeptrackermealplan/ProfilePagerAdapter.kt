package com.example.sleeptrackermealplan

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// This adapter provides the fragments for the ViewPager2 in the Profile screen.
class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // We have two tabs: "My Posts" and "Likes"

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // Pass "my_posts" for the first tab
            0 -> PostListFragment.newInstance("my_posts")
            // Pass "liked_posts" for the second tab
            1 -> PostListFragment.newInstance("liked_posts")
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

