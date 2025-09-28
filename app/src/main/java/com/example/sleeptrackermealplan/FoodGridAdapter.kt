package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptrackermealplan.databinding.ItemFoodChoiceBinding

// This is a simple data class to represent a food item.
// You can expand this later with more details like recipe text, video URLs, etc.
data class FoodItem(
    val id: Int,
    val name: String,
    val calories: Int,
    val imageResId: Int,
    val recipeDetails: String, // <-- ADD THIS
    val videoUrl: String      // <-- ADD THIS
)

class FoodGridAdapter(
    private val foodItems: List<FoodItem>,
    private val onItemClicked: (FoodItem) -> Unit // This is a lambda function to handle clicks
) : RecyclerView.Adapter<FoodGridAdapter.FoodViewHolder>() {

    // The ViewHolder holds references to the views for each item.
    class FoodViewHolder(val binding: ItemFoodChoiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        // Inflate the item layout (item_food_choice.xml)
        val binding = ItemFoodChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFoodItem = foodItems[position]

        // Set the image for the current food item.
        holder.binding.imageFoodChoice.setImageResource(currentFoodItem.imageResId)

        // Set up the click listener.
        // When an item is clicked, it will call the lambda function we passed in.
        holder.itemView.setOnClickListener {
            onItemClicked(currentFoodItem)
        }
    }

    override fun getItemCount(): Int {
        // Return the total number of items in the list.
        return foodItems.size
    }
}

