package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeptrackermealplan.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    // Initialize the ViewModel
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigationClicks()
        setupArticleRecycler()
    }

    private fun setupNavigationClicks() {
        binding.viewProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_profile)
        }
        binding.communityCard.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_community)
        }
        binding.rankingCard.setOnClickListener {
            findNavController().navigate(R.id.action_library_to_ranking)
        }
    }

    private fun setupArticleRecycler() {
        // Create the adapter and handle article clicks
        val articleAdapter = ArticleAdapter { article ->
            // --- THIS IS THE FIX ---
            // We now navigate directly without passing any data,
            // because ArticleDetailFragment has a hardcoded URL.
            findNavController().navigate(R.id.action_library_to_articleDetail)
        }

        // Setup the RecyclerView
        binding.articlesRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }

        // Observe the list of articles from the ViewModel
        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

