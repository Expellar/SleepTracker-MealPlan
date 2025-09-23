package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.R
import com.google.android.material.button.MaterialButton

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the XML layout
        return inflater.inflate(R.layout.fragment_sleep_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 😴 Sleep button
        val sleepButton: MaterialButton = view.findViewById(R.id.sleep_button)
        sleepButton.setOnClickListener {
            // TODO: Add logic for starting sleep tracking
        }

        // ☀️ Wake Up button
        val wakeUpButton: MaterialButton = view.findViewById(R.id.wake_up_button)
        wakeUpButton.setOnClickListener {
            // TODO: Add logic for waking up / ending sleep tracking
        }
    }
}
