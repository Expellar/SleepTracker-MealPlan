package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentDailyReviewBinding

class DailyReviewFragment : Fragment() {

    private var _binding: FragmentDailyReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the date passed from the ProgressFragment
        val selectedDate = arguments?.getString("selectedDate") ?: "Review"

        // Setup Toolbar
        binding.toolbar.title = "Review for $selectedDate"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // TODO: Load and display the actual data for the selected date
        // For now, we'll just show placeholder data.
        binding.textSleepScoreValue.text = "85"
        binding.textCalorieIntakeValue.text = "2,100 kcal"
        binding.textHealthyMealsValue.text = "3 / 3"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
