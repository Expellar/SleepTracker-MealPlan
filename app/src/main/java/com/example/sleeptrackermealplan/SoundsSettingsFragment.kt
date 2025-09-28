package com.example.sleeptrackermealplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentSoundsSettingsBinding

class SoundsSettingsFragment : Fragment() {

    // Using View Binding to safely access the views
    private var _binding: FragmentSoundsSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentSoundsSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the click listener for the back button
        binding.imageBack.setOnClickListener {
            // This will navigate back to the previous screen (SettingsFragment)
            findNavController().navigateUp()
        }

//        // You can add logic for your RadioButtons here, for example:
//        binding.radiogroupSoundAlarm.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radio_sound_10s -> {
//                    // Handle "10 sec" selection
//                }
//                R.id.radio_sound_5s -> {
//                    // Handle "5 sec" selection
//                }
//                R.id.radio_sound_off -> {
//                    // Handle "off" selection
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }
}
