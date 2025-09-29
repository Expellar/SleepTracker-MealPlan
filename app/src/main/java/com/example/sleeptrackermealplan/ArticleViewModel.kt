package com.example.sleeptrackermealplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptrackermealplan.data.Article

class ArticleViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    init {
        loadArticles()
    }

    private fun loadArticles() {
        // In a real app, you would fetch these from a server or a local database.
        val sampleArticles = listOf(
            Article(
                title = "15 Tips to Sleep Better at Night",
                description = "Evidence-based tips to improve your sleep quality.",
                imageUrl = "https://images.unsplash.com/photo-1595147596378-d146162c44c7?q=80&w=2070&auto=format&fit=crop",
                articleUrl = "https://www.healthline.com/nutrition/17-tips-to-sleep-better"
            ),
            Article(
                title = "Heart-Healthy Eating Pattern",
                description = "Discover the best foods for a healthy heart and lifestyle.",
                imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=2070&auto=format&fit=crop",
                articleUrl = "https://www.heartfoundation.org.au/healthy-living/healthy-eating/heart-healthy-eating-pattern"
            )
        )
        _articles.value = sampleArticles
    }
}
