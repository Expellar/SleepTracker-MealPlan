package com.example.sleeptrackermealplan

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeptrackermealplan.data.DailyReview
import com.example.sleeptrackermealplan.data.ProgressStats
import com.example.sleeptrackermealplan.databinding.FragmentProgressBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgressViewModel by viewModels()
    private lateinit var achievementAdapter: AchievementAdapter
    private lateinit var weeklyReviewAdapter: WeeklyReviewAdapter

    // --- Calendar State ---
    private val calendarDates = mutableListOf<DailyReview>()
    private var currentlySelectedDate: DailyReview? = null
    private val daysInPast = 180 // 6 months
    private val totalDays = 365 // 1 year total

    // --- Image Picker ---
    private var selectedWeightImageType: String? = null
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (selectedWeightImageType == "start") {
                binding.startingWeightImage.setImageURI(it)
            } else if (selectedWeightImageType == "current") {
                binding.currentWeightImage.setImageURI(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPeriodClickListeners()
        setupWeightJourneyClicks()
        setupAchievements()
        setupNavigation()
        setupWeeklyReviewCalendar()
        observeViewModel()
    }

    private fun setupNavigation() {
        binding.achievementsMoreButton.setOnClickListener {
            findNavController().navigate(R.id.action_progressFragment_to_achievementsFragment)
        }
        binding.btnOpenCalendar.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupWeeklyReviewCalendar() {
        calendarDates.clear()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysInPast)

        for (i in 0..totalDays) {
            val isToday = i == daysInPast
            val status = viewModel.getRandomReviewStatus()
            calendarDates.add(DailyReview(calendar.clone() as Calendar, status, isSelected = isToday))
            // --- THIS LINE WAS MISSING, CAUSING ALL DATES TO BE THE SAME ---
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        currentlySelectedDate = calendarDates.find { it.isSelected }

        weeklyReviewAdapter = WeeklyReviewAdapter(calendarDates) { selectedDate, position ->
            if (selectedDate == currentlySelectedDate) {
                // SECOND CLICK: Navigate to detail screen
                val dateString = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(selectedDate.calendar.time)
                val bundle = bundleOf("selectedDate" to dateString)
                findNavController().navigate(R.id.action_progressFragment_to_dailyReviewFragment, bundle)
            } else {
                // FIRST CLICK: Select and scroll to the left edge
                val oldIndex = calendarDates.indexOf(currentlySelectedDate)
                if (oldIndex != -1) {
                    calendarDates[oldIndex].isSelected = false
                    weeklyReviewAdapter.notifyItemChanged(oldIndex)
                }

                selectedDate.isSelected = true
                weeklyReviewAdapter.notifyItemChanged(position)

                // This command scrolls the item to the very beginning of the list
                (binding.recyclerViewWeeklyReview.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)

                updateDateTitle(selectedDate)
                currentlySelectedDate = selectedDate
            }
        }

        binding.recyclerViewWeeklyReview.adapter = weeklyReviewAdapter
        // Scroll to the initial position
        val initialPosition = calendarDates.indexOf(currentlySelectedDate)
        if(initialPosition != -1) {
            binding.recyclerViewWeeklyReview.scrollToPosition(initialPosition)
        }

        currentlySelectedDate?.let { updateDateTitle(it) }
    }

    private fun showDatePickerDialog() {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val newSelectedCalendar = Calendar.getInstance()
                newSelectedCalendar.set(year, month, dayOfMonth)
                updateCalendarSelection(newSelectedCalendar)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateCalendarSelection(newDate: Calendar) {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_YEAR, -daysInPast)

        val diff = newDate.timeInMillis - startDate.timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(diff).toInt()

        if (days >= 0 && days < calendarDates.size) {
            // This is a jump, so we just select and scroll, not a two-stage click
            val oldIndex = calendarDates.indexOfFirst { it.isSelected }
            if (oldIndex != -1) {
                calendarDates[oldIndex].isSelected = false
                weeklyReviewAdapter.notifyItemChanged(oldIndex)
            }
            calendarDates[days].isSelected = true
            weeklyReviewAdapter.notifyItemChanged(days)
            (binding.recyclerViewWeeklyReview.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(days, 0)
            updateDateTitle(calendarDates[days])
            currentlySelectedDate = calendarDates[days]
        }
    }

    private fun updateDateTitle(date: DailyReview) {
        val titleFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        binding.weeklyReviewDates.text = titleFormat.format(date.calendar.time)
    }

    private fun setupPeriodClickListeners() {
        val periods = listOf(binding.period1w, binding.period1m, binding.period6m, binding.period1y, binding.periodAll)
        periods.forEach { periodTextView ->
            periodTextView.setOnClickListener {
                viewModel.updatePeriod(periodTextView.text.toString())
                updatePeriodSelection(periodTextView, periods)
            }
        }
    }

    private fun updatePeriodSelection(selectedView: TextView, allViews: List<TextView>) {
        allViews.forEach { view ->
            if (view.id == selectedView.id) {
                view.setBackgroundResource(R.drawable.period_selected_background)
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
            } else {
                view.setBackgroundResource(android.R.color.transparent)
                view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
            }
        }
    }

    private fun setupWeightJourneyClicks() {
        binding.startingWeightImage.setOnClickListener {
            selectedWeightImageType = "start"
            pickImageLauncher.launch("image/*")
        }
        binding.currentWeightImage.setOnClickListener {
            selectedWeightImageType = "current"
            pickImageLauncher.launch("image/*")
        }
    }

    private fun setupAchievements() {
        achievementAdapter = AchievementAdapter()
        binding.achievementsRecyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = achievementAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.progressStats.observe(viewLifecycleOwner) { stats ->
            updateStatsUI(stats)
        }
        viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
            achievementAdapter.submitList(achievements)
        }
        viewModel.weeklyReview.observe(viewLifecycleOwner) { reviews ->
            // This can be used later if needed
        }
    }

    private fun updateStatsUI(stats: ProgressStats) {
        binding.stat1Value.text = stats.avgSleepScore.toString()
        binding.stat2Value.text = stats.avgSleepHours.toString()
        binding.stat3Value.text = stats.goalStreak.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

