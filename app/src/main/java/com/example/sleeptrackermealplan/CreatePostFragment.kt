package com.example.sleeptrackermealplan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.sleeptrackermealplan.databinding.FragmentCreatePostBinding
import java.util.*

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private val postViewModel: PostViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            loadRecentImages()
        } else {
            Toast.makeText(context, "Permission denied to read photos.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.buttonAddImage.setOnClickListener { pickImageLauncher.launch("image/*") }
        binding.buttonPost.setOnClickListener { createPost() }
        binding.buttonRemoveImage.setOnClickListener { removeSelectedImage() } // Listener for the remove button

        checkPermissionsAndLoadImages()
    }

    private fun checkPermissionsAndLoadImages() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                loadRecentImages()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun loadRecentImages() {
        val recentImages = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext() && recentImages.size < 20) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                recentImages.add(contentUri)
            }
        }

        binding.recyclerViewGallery.adapter = GalleryImageAdapter(recentImages) { imageUri ->
            onImageSelected(imageUri)
        }
    }

    private fun onImageSelected(uri: Uri) {
        selectedImageUri = uri
        // Load the image into the preview and make it visible
        Glide.with(this).load(uri).into(binding.imagePreview)
        binding.previewContainer.visibility = View.VISIBLE
    }

    private fun removeSelectedImage() {
        selectedImageUri = null
        // Hide the preview container
        binding.previewContainer.visibility = View.GONE
    }


    private fun createPost() {
        val postText = binding.editTextPostContent.text.toString().trim()
        if (postText.isBlank() && selectedImageUri == null) {
            Toast.makeText(context, "Cannot create an empty post.", Toast.LENGTH_SHORT).show()
            return
        }

        val newPost = Post(
            id = UUID.randomUUID().toString(),
            userName = "You",
            userAvatarUrl = "https://placehold.co/100x100/EFEFEF/AAAAAA&text=Y",
            timestamp = System.currentTimeMillis(),
            postContent = postText,
            imageUrl = selectedImageUri?.toString(),
            isLiked = false
        )

        postViewModel.addPost(newPost)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

