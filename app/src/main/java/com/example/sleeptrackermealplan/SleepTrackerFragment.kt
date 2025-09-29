package com.example.sleeptrackermealplan

import android.app.DatePickerDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentSleepTrackerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

// Data class to hold the sleep data for a single day
data class DailySleepData(
    var sleepTimeStartMillis: Long? = null,
    var wakeTimeMillis: Long? = null,
    var sleepDurationHours: Float = 0f,
    var sleepScore: Int = 0
)

class SleepTrackerFragment : Fragment() {

    private var _binding: FragmentSleepTrackerBinding? = null
    private val binding get() = _binding!!

    // --- State Management ---
    private var selectedDate: CalendarDate? = null
    private lateinit var dateAdapter: DateAdapter
    private val calendarDates = mutableListOf<CalendarDate>()
    private val daysInPast = 365

    // --- Media Player for Soundtracks ---
    private var mediaPlayer: MediaPlayer? = null
    private var selectedSoundOption: View? = null

    // --- Data Persistence ---
    private val sleepDataHistory = HashMap<String, DailySleepData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
        setupSoundOptions()

        // ✅ FIX: Restored the logic for all buttons
        binding.btnOpenCalendar.setOnClickListener { showDatePickerDialog() }

        binding.sleepButton.setOnClickListener {
            val dateKey = getDateKey(selectedDate!!.calendar)
            val sleepData = sleepDataHistory.getOrPut(dateKey) { DailySleepData() }
            sleepData.sleepTimeStartMillis = System.currentTimeMillis()
            // Reset wake time in case user starts a new session
            sleepData.wakeTimeMillis = null
            calculateSleepStats(sleepData) // Recalculate stats
            updateSleepUiForDate(selectedDate!!)
        }

        binding.wakeUpButton.setOnClickListener {
            val dateKey = getDateKey(selectedDate!!.calendar)
            val sleepData = sleepDataHistory.get(dateKey)
            // Only allow wake up if a sleep session has started and not ended
            if (sleepData?.sleepTimeStartMillis != null && sleepData.wakeTimeMillis == null) {
                sleepData.wakeTimeMillis = System.currentTimeMillis()
                calculateSleepStats(sleepData)
                updateSleepUiForDate(selectedDate!!)
            }
        }

        binding.btnSetSleepReminder.setOnClickListener {
            findNavController().navigate(R.id.action_sleepTrackerFragment_to_sleepRemindersFragment)
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupSoundOptions() {
        binding.optionNature.setOnClickListener { handleSoundSelection(it, R.raw.nature) }
        binding.optionRain.setOnClickListener { handleSoundSelection(it, R.raw.rain) }
        binding.optionFocus.setOnClickListener { handleSoundSelection(it, R.raw.focus) }
        binding.optionMeditation.setOnClickListener { handleSoundSelection(it, R.raw.meditation) }
        binding.optionCalm.setOnClickListener { handleSoundSelection(it, R.raw.calm) }
    }

    private fun handleSoundSelection(clickedView: View, soundResId: Int) {
        // If a sound is already playing and the user taps the same icon, stop it.
        if (selectedSoundOption == clickedView) {
            stopSound()
            return
        }

        // If a different sound is playing, stop it first.
        stopSound()

        // Start the new sound
        mediaPlayer = MediaPlayer.create(requireContext(), soundResId)
        mediaPlayer?.isLooping = true // Loop the sound
        mediaPlayer?.start()

        // Update the UI
        setSelectedSoundUI(clickedView as LinearLayout)
        selectedSoundOption = clickedView
    }

    private fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        resetSoundUI()
        selectedSoundOption = null
    }

    private fun setSelectedSoundUI(selectedLayout: LinearLayout) {
        // Get the ImageView and TextView from the selected layout
        val icon = selectedLayout.getChildAt(0) as ImageView
        val text = selectedLayout.getChildAt(1) as TextView
        // Set the tint to the accent color (orange)
        icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange))
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
    }

    private fun resetSoundUI() {
        // If a sound was previously selected, reset its UI
        (selectedSoundOption as? LinearLayout)?.let {
            val icon = it.getChildAt(0) as ImageView
            val text = it.getChildAt(1) as TextView
            // Set the tint back to the default gray color
            val grayColor = ContextCompat.getColor(requireContext(), R.color.gray_text)
            icon.setColorFilter(grayColor)
            text.setTextColor(grayColor)
        }
    }

    override fun onStop() {
        super.onStop()
        // Stop any playing sound when the fragment is not visible
        stopSound()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Ensure the media player is released
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }

    // ... All other functions (setupCalendar, updateSleepUiForDate, etc.) remain here ...
    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        val totalDays = 730

        calendar.add(Calendar.DAY_OF_YEAR, -daysInPast)

        for (i in 0..totalDays) {
            val isToday = i == daysInPast
            calendarDates.add(CalendarDate(calendar.clone() as Calendar, isSelected = isToday))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        selectedDate = calendarDates.find { it.isSelected }

        dateAdapter = DateAdapter(calendarDates) { date ->
            selectedDate = date
            updateSleepUiForDate(date)
        }

        binding.dateRecyclerView.adapter = dateAdapter
        binding.dateRecyclerView.scrollToPosition(daysInPast)

        selectedDate?.let { updateSleepUiForDate(it) }
    }

    private fun updateSleepUiForDate(date: CalendarDate) {
        val dateKey = getDateKey(date.calendar)
        val sleepData = sleepDataHistory.get(dateKey)

        if (sleepData != null) {
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            binding.timeInBedValue.text = if(sleepData.sleepTimeStartMillis != null) timeFormat.format(sleepData.sleepTimeStartMillis) else "--:--"
            binding.wakeUpValue.text = if(sleepData.wakeTimeMillis != null) timeFormat.format(sleepData.wakeTimeMillis) else "--:--"

            binding.sleepTimeValue.text = "${String.format("%.1f", sleepData.sleepDurationHours)} HRS"
            binding.sleepScoreValue.text = sleepData.sleepScore.toString()
            binding.sleepScoreProgress.progress = sleepData.sleepScore
        } else {
            // No data for this date, reset to default state
            binding.timeInBedValue.text = "--:--"
            binding.wakeUpValue.text = "--:--"
            binding.sleepTimeValue.text = "0.0 HRS"
            binding.sleepScoreValue.text = "0"
            binding.sleepScoreProgress.progress = 0
        }
    }

    private fun getDateKey(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun showDatePickerDialog() {
        val today = selectedDate?.calendar ?: Calendar.getInstance()
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
            // Deselect old date
            val oldIndex = calendarDates.indexOfFirst { it.isSelected }
            if (oldIndex != -1) {
                calendarDates[oldIndex].isSelected = false
                dateAdapter.notifyItemChanged(oldIndex)
            }
            // Select new date
            calendarDates[days].isSelected = true
            selectedDate = calendarDates[days]
            dateAdapter.notifyItemChanged(days)
            binding.dateRecyclerView.scrollToPosition(days)

            updateSleepUiForDate(calendarDates[days])
        }
    }

    private fun calculateSleepStats(sleepData: DailySleepData) {
        if (sleepData.sleepTimeStartMillis != null && sleepData.wakeTimeMillis != null) {
            val durationMillis = sleepData.wakeTimeMillis!! - sleepData.sleepTimeStartMillis!!
            sleepData.sleepDurationHours = TimeUnit.MILLISECONDS.toMinutes(durationMillis) / 60f

            // Simple sleep score calculation (e.g., 8 hours = 100)
            val idealHours = 8f
            val score = ((sleepData.sleepDurationHours / idealHours) * 100).toInt()
            sleepData.sleepScore = score.coerceIn(0, 100)
        } else {
            // If only start time is set, reset stats
            sleepData.sleepDurationHours = 0f
            sleepData.sleepScore = 0
        }
    }
}

