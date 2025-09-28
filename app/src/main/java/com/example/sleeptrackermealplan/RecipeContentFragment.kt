package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeContentFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // In a real app, you would inflate a proper layout file here.
        return TextView(requireContext()).apply {
            text = "Recipe details will be shown here."
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }
    }
}
