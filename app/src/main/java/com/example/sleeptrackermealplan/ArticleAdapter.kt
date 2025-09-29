package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sleeptrackermealplan.data.Article
import com.example.sleeptrackermealplan.databinding.ListItemArticleBinding

class ArticleAdapter(private val onClick: (Article) -> Unit) :
    ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback) {

    class ArticleViewHolder(private val binding: ListItemArticleBinding, val onClick: (Article) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentArticle: Article? = null

        init {
            binding.root.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }

        fun bind(article: Article) {
            currentArticle = article
            binding.articleTitle.text = article.title
            binding.articleSubtitle.text = article.description

            Glide.with(binding.root.context)
                .load(article.imageUrl)
                .centerCrop()
                .into(binding.articleImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ListItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }
}

object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.articleUrl == newItem.articleUrl
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
