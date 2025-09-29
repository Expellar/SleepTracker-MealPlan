package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentArticleDetailBinding

class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Setup Toolbar ---
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // --- Load the Hardcoded URL ---
        // We removed the 'navArgs' and are loading a fixed URL for now.
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl("https://www.healthline.com/nutrition/17-tips-to-sleep-better")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

