package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentLibraryProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var _binding: FragmentLibraryProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the back button logic on the Toolbar
        binding.toolbar.setNavigationOnClickListener {
            // Correctly navigate back to the previous screen
            findNavController().popBackStack()
        }

        // Set up the Edit Profile button
        binding.editProfileButton.setOnClickListener {
            // TODO: Implement navigation to Edit Profile screen
        }

        // --- ViewPager2 and TabLayout Setup (The part you requested) ---

        // 1. Initialize the adapter
        val profilePagerAdapter = ProfilePagerAdapter(this)
        binding.viewPager.adapter = profilePagerAdapter

        // 2. Define the tab titles
        val tabTitles = arrayOf("My Posts", "Likes")

        // 3. Connect the TabLayout and ViewPager2 using TabLayoutMediator
        // This synchronizes the scrolling and clicking between the two components.
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object reference to prevent memory leaks
        _binding = null
    }
}
