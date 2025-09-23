package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {

    // View Binding variable to safely access views
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listener for the back arrow
        binding.backArrow.setOnClickListener {
            // This will navigate the user to the previous screen
            findNavController().navigateUp()
        }

        // You can also load user data here
        loadUserProfile()
    }

    private fun loadUserProfile() {
        // In the future, you would load real user data from a database or API here.
        // For now, it just uses the placeholder data from the XML.
        // Example:
        // binding.profileName.text = user.name
        // binding.followerCount.text = user.followers.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to prevent memory leaks
        _binding = null
    }
}