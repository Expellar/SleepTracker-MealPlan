package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sleeptrackermealplan.databinding.FragmentCommunityPostBinding
import com.google.android.material.snackbar.Snackbar


class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Logic for Community Screen ---

        // TODO: 1. Setup the RecyclerView with an Adapter for posts
        // You'll fetch data from a ViewModel and submit it to the adapter.
        // e.g., binding.communityRecyclerview.adapter = CommunityPostAdapter()

        // TODO: 2. Handle the Floating Action Button click
        // This should probably navigate to a new "CreatePostFragment"
        binding.fabCreatePost.setOnClickListener {
            // For now, we'll just show a message
            Snackbar.make(view, "Create new post clicked!", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
