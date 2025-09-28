package com.example.sleeptrackermealplan

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentMealRemindersBinding
import java.text.SimpleDateFormat
import java.util.*

class MealRemindersFragment : Fragment() {

    // Using view binding to safely access views
    private var _binding: FragmentMealRemindersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the back button to navigate to the previous screen
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Set up click listeners for each time TextView to show the time picker
        setupTimePicker(binding.textTimeBreakfast)
        setupTimePicker(binding.textTimeLunch)
        setupTimePicker(binding.textTimeDinner)
    }

    /**
     * Sets up a click listener for a TextView to show a TimePickerDialog.
     * @param textView The TextView that will trigger the dialog and display the selected time.
     */
    private fun setupTimePicker(textView: TextView) {
        textView.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Create a TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    // This is called when the user sets a time
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    // Format the time to a user-friendly "hh:mm a" format (e.g., 08:30 AM)
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val formattedTime = timeFormat.format(selectedTime.time)

                    // Update the TextView with the new time
                    textView.text = formattedTime
                },
                // Set the initial time for the picker
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // Use false for 12-hour format with AM/PM
            )
            // Show the dialog
            timePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding reference to avoid memory leaks
        _binding = null
    }
}
