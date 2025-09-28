package com.example.sleeptrackermealplan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecipePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecipeContentFragment()
            1 -> VideoTutorialFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
