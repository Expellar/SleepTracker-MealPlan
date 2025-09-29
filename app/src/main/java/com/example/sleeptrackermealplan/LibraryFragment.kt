package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentLibraryBinding


class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- UPDATED Navigation Logic ---

        // 1. Navigate to Profile when "View Profile" is clicked
        binding.featuredMemberCard.viewProfileButton.setOnClickListener {
            // Use the new action ID from nav_graph_main_tabs.xml
            findNavController().navigate(R.id.action_library_to_profile)
        }

        // 2. Navigate to Community when "Community and Sharing" is clicked
        binding.communityCard.setOnClickListener {
            // Use the new action ID from nav_graph_main_tabs.xml
            findNavController().navigate(R.id.action_library_to_community)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
