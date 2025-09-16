package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentWellnessBinding

class WellnessFragment : Fragment() {

    private var _binding: FragmentWellnessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWellnessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to Meal Plan when card clicked
        binding.cardMealPlan.setOnClickListener {
            findNavController().navigate(R.id.nav_meal_plan)
        }

//        // Navigate to Sleep Tracker (you’ll add the fragment later)
//        binding.cardSleepTracker.setOnClickListener {
//            findNavController().navigate(R.id.nav_sleep_tracker)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
