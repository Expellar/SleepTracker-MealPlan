package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sleeptrackermealplan.databinding.FragmentProgressBinding

class ProgressFragment : Fragment() {
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Progress"

        // Set up the click listeners
        binding.period1w.setOnClickListener { updateStatsView("1W") }
        binding.period1m.setOnClickListener { updateStatsView("1M") }
        binding.period6m.setOnClickListener { updateStatsView("6M") }
        binding.period1y.setOnClickListener { updateStatsView("1Y") }
        binding.periodAll.setOnClickListener { updateStatsView("All") }

        // Initial state
        updateStatsView("1W")
    }

    private fun updateStatsView(selectedPeriod: String) {
        // --- Part 1: Update the stat numbers with fake data (same as before) ---
        when (selectedPeriod) {
            "1W" -> {
                binding.stat1Value.text = "82"
                binding.stat2Value.text = "7.8"
                binding.stat3Value.text = "12"
            }
            "1M" -> {
                binding.stat1Value.text = "79"
                binding.stat2Value.text = "7.5"
                binding.stat3Value.text = "25"
            }
            "6M" -> {
                binding.stat1Value.text = "85"
                binding.stat2Value.text = "8.1"
                binding.stat3Value.text = "150"
            }
            "1Y" -> {
                binding.stat1Value.text = "81"
                binding.stat2Value.text = "7.9"
                binding.stat3Value.text = "280"
            }
            "All" -> {
                binding.stat1Value.text = "83"
                binding.stat2Value.text = "8.0"
                binding.stat3Value.text = "350"
            }
        }

        // --- Part 2: Update the visual style of the time period labels ---
        val timeLabels = listOf(
            binding.period1w,
            binding.period1m,
            binding.period6m,
            binding.period1y,
            binding.periodAll
        )

        val selectedColor = ContextCompat.getColor(requireContext(), R.color.orange)
        val defaultColor = ContextCompat.getColor(requireContext(), android.R.color.darker_gray)

        for (label in timeLabels) {
            // We use the label's text content (e.g., "1W") to identify it
            if (label.text.toString() == selectedPeriod) {
                // Apply the selected style
                label.setTextColor(selectedColor)
                label.setBackgroundResource(R.drawable.orange_dot)
            } else {
                // Apply the default/unselected style
                label.setTextColor(defaultColor)
                label.setBackgroundResource(android.R.color.transparent) // This removes the dot
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

