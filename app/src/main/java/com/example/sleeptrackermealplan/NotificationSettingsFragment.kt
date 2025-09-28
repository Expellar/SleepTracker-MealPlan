package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment() {

    // Using view binding to safely access views
    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listener for the back button
        binding.imageBack.setOnClickListener {
            // Navigate up to the previous screen in the back stack (SettingsFragment)
            findNavController().navigateUp()
        }

        // TODO: Add listeners for your switches here, for example:
        // binding.switchBedtimeReminders.setOnCheckedChangeListener { _, isChecked ->
        //     if (isChecked) {
        //         // Handle when the switch is turned ON
        //     } else {
        //         // Handle when the switch is turned OFF
        //     }
        // }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance to prevent memory leaks
        _binding = null
    }
}
