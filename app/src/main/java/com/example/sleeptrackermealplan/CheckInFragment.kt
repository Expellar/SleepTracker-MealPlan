package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentCheckInBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckinFragment : Fragment() {

    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Logic to set the current date ---
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        binding.checkinDate.text = currentDate

        // --- Navigation Logic ---
        binding.nextButton.setOnClickListener {
            // ✅ FIX: This line is now active and will perform the navigation.
            findNavController().navigate(R.id.action_checkInFragment_to_main_tabs)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

