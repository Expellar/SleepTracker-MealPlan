package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleeptrackermealplan.databinding.FragmentPostListBinding

class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()
    private lateinit var postAdapter: PostAdapter
    private var listType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { listType = it.getString(ARG_LIST_TYPE) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        postViewModel.posts.observe(viewLifecycleOwner) { allPosts ->
            // --- FILTERING LOGIC IS NOW HERE ---
            val filteredPosts = when (listType) {
                "liked_posts" -> allPosts.filter { it.isLiked }
                "my_posts" -> allPosts.filter { it.userName == "You" } // Simple filter for user's posts
                else -> allPosts // For "For You" and "Following", show all for now
            }
            postAdapter.submitList(filteredPosts.toMutableList())
        }
    }

    private fun setupRecyclerView() {
        // Pass the ViewModel's toggle function to the adapter
        postAdapter = PostAdapter { postId ->
            postViewModel.toggleLikeStatus(postId)
        }
        binding.recyclerViewPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_LIST_TYPE = "list_type"
        fun newInstance(listType: String): PostListFragment {
            return PostListFragment().apply {
                arguments = Bundle().apply { putString(ARG_LIST_TYPE, listType) }
            }
        }
    }
}

