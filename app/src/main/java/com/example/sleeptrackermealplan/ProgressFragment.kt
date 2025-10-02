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
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeptrackermealplan.data.DailyReview
import com.example.sleeptrackermealplan.data.ProgressStats
import com.example.sleeptrackermealplan.databinding.FragmentProgressBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
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

    private var currentlySelectedDate: DailyReview? = null
    private val calendarDates = mutableListOf<DailyReview>()
    private val daysInPast = 180
    private val totalDays = 365

    // --- NEW PHOTO SELECTION LOGIC ---
    private var selectedWeightImageType: String? = null
    private var tempImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { updateWeightImage(it) }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            tempImageUri?.let { updateWeightImage(it) }
        }
    }
    // --- END OF NEW LOGIC ---

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

    private fun setupWeightJourneyClicks() {
        binding.startingWeightImage.setOnClickListener {
            selectedWeightImageType = "start"
            showPhotoSourceDialog()
        }
        binding.currentWeightImage.setOnClickListener {
            selectedWeightImageType = "current"
            showPhotoSourceDialog()
        }
    }

    // --- NEW FUNCTION TO SHOW THE BOTTOM SHEET ---
    private fun showPhotoSourceDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_photo_source, null)
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.option_take_photo).setOnClickListener {
            tempImageUri = createImageUri()
            cameraLauncher.launch(tempImageUri)
            dialog.dismiss()
        }

        view.findViewById<TextView>(R.id.option_choose_gallery).setOnClickListener {
            galleryLauncher.launch("image/*")
            dialog.dismiss()
        }
        dialog.show()
    }

    // --- NEW HELPER FUNCTIONS ---
    private fun createImageUri(): Uri {
        val image = File(requireContext().cacheDir, "progress_photo_${System.currentTimeMillis()}.png")
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", image)
    }

    private fun updateWeightImage(uri: Uri) {
        if (selectedWeightImageType == "start") {
            binding.startingWeightImage.setImageURI(uri)
        } else if (selectedWeightImageType == "current") {
            binding.currentWeightImage.setImageURI(uri)
        }
    }
    // --- END OF NEW HELPERS ---


    // --- Other Fragment functions (unchanged) ---
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
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        currentlySelectedDate = calendarDates.find { it.isSelected }

        weeklyReviewAdapter = WeeklyReviewAdapter(calendarDates) { selectedDate, position ->
            if (selectedDate == currentlySelectedDate) {
                val dateString = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(selectedDate.calendar.time)
                val bundle = bundleOf("selectedDate" to dateString)
                findNavController().navigate(R.id.action_progressFragment_to_dailyReviewFragment, bundle)
            } else {
                val oldIndex = calendarDates.indexOf(currentlySelectedDate)
                if (oldIndex != -1) {
                    calendarDates[oldIndex].isSelected = false
                    weeklyReviewAdapter.notifyItemChanged(oldIndex)
                }

                selectedDate.isSelected = true
                weeklyReviewAdapter.notifyItemChanged(position)
                (binding.recyclerViewWeeklyReview.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0)
                updateDateTitle(selectedDate)
                currentlySelectedDate = selectedDate
            }
        }

        binding.recyclerViewWeeklyReview.adapter = weeklyReviewAdapter
        val initialPosition = calendarDates.indexOf(currentlySelectedDate)
        if (initialPosition != -1) {
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
            // Logic handled by the RecyclerView
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

