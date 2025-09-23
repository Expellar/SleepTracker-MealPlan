package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the title for the screen
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"

        // ✅ SET ONCLICK LISTENER FOR "MY PROFILE"
        binding.settingProfile.setOnClickListener {
            // This tells the NavController to perform the action we defined in the nav graph
            findNavController().navigate(R.id.action_settingsFragment_to_myProfileFragment)
        }

        // You can add listeners for the other settings items here as well
        // binding.settingUnits.setOnClickListener { ... }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}