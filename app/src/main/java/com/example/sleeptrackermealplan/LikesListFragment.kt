package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Fragment to display the list of the user's liked items (Tab 2 content).
 * This uses a basic RecyclerView setup similar to PostListFragment.
 */
class LikesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = LikesAdapter(generateMockLikes(15))
        return recyclerView
    }

    data class Like(val id: Int, val title: String)

    private fun generateMockLikes(count: Int): List<Like> {
        return (1..count).map {
            Like(it, "Liked Item Title $it (e.g., Article, Recipe)")
        }
    }

    class LikesAdapter(private val likes: List<Like>) : RecyclerView.Adapter<LikesAdapter.LikeViewHolder>() {

        class LikeViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
            val textView = android.widget.TextView(parent.context).apply {
                textSize = 16f
                setPadding(16, 16, 16, 16)
                setTextColor(android.graphics.Color.WHITE)
                setBackgroundColor(android.graphics.Color.parseColor("#303030")) // Slightly different background for visibility
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return LikeViewHolder(textView)
        }

        override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
            (holder.view as android.widget.TextView).text = "Liked: ${likes[position].title}"
        }

        override fun getItemCount() = likes.size
    }
}
