package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class VideoTutorialFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // In a real app, you would inflate a layout with a WebView or YouTube player.
        return TextView(requireContext()).apply {
            text = "A video tutorial link or player will be shown here."
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }
    }
}
