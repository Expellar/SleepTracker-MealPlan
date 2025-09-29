package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sleeptrackermealplan.databinding.FragmentForYouBinding

class ForYouFragment : Fragment() {

    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Set up RecyclerView adapter here to display posts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
