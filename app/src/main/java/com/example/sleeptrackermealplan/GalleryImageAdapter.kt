package com.example.sleeptrackermealplan

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sleeptrackermealplan.databinding.ListItemGalleryImageBinding

class GalleryImageAdapter(
    private val images: List<Uri>,
    private val onImageClick: (Uri) -> Unit
) : RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(private val binding: ListItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: Uri, onImageClick: (Uri) -> Unit) {
            Glide.with(itemView.context)
                .load(imageUri)
                .into(binding.galleryImage)
            itemView.setOnClickListener { onImageClick(imageUri) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ListItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], onImageClick)
    }

    override fun getItemCount(): Int = images.size
}
