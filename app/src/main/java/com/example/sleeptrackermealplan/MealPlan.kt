package com.example.sleeptrackermealplan // Correct package name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sleeptrackermealplan.databinding.ActivityMealPlanBinding // Correct binding import

// Correct class name
class MealPlan : AppCompatActivity() {

    // Use the correct binding class type
    private lateinit var binding: ActivityMealPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using the correct binding class
        binding = ActivityMealPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This line requires the color 'app_background_dark' in your colors.xml file
        window.statusBarColor = getColor(R.color.app_background_dark)

        // These lines require the IDs 'bottom_navigation' and 'iv_back_arrow' in your XML layout
        // and 'nav_wellness' in your menu file.


    }
}