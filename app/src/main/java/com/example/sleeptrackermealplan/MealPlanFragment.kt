package com.example.sleeptrackermealplan

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sleeptrackermealplan.databinding.FragmentMealPlanBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

// Data class to hold the complete meal plan for a single day
data class DailyMealPlan(
    var breakfast: FoodItem?,
    var lunch: FoodItem?,
    var dinner: FoodItem?
)

class MealPlanFragment : Fragment() {

    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!

    // --- State Management ---
    private enum class RightPanelState { TUTORIAL, SELECTION, DETAIL }
    private enum class MealType { BREAKFAST, LUNCH, DINNER }
    private var currentMealType: MealType = MealType.BREAKFAST
    private var selectedFoodItem: FoodItem? = null
    private var selectedDate: CalendarDate? = null
    private lateinit var dateAdapter: DateAdapter
    private val calendarDates = mutableListOf<CalendarDate>()
    private val daysInPast = 365 // One year in the past

    // --- Data Persistence ---
    private val mealPlanHistory = HashMap<String, DailyMealPlan>()

    // --- Mock Data ---
    private val breakfastOptions = listOf(
        FoodItem(1, "Avocado & Egg Toast", 350, R.drawable.avocado_egg_toast, "1. Toast bread...", "https://..."),
        FoodItem(2, "Pancakes", 450, R.drawable.pancake, "1. Mix flour...", "https://...")
    )
    private val lunchOptions = listOf(
        FoodItem(4, "Chicken Salad", 420, R.drawable.chicken_salad, "1. Grill chicken...", "https://..."),
    )
    private val dinnerOptions = listOf(
        FoodItem(6, "Grilled Salmon", 550, R.drawable.grilled_salmon, "1. Season salmon...", "https://..."),
    )

    private val defaultBreakfast = FoodItem(0, "Select Breakfast", 0, R.drawable.pancake, "", "")
    private val defaultLunch = FoodItem(0, "Select Lunch", 0, R.drawable.chicken_salad, "", "")
    private val defaultDinner = FoodItem(0, "Select Dinner", 0, R.drawable.rib_eye_steak_au_poivre_with_roasted_veggies, "", "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
        updateRightPanel(RightPanelState.TUTORIAL, false)

        // --- Click Listeners ---
        binding.btnOpenCalendar.setOnClickListener {
            showDatePickerDialog()
        }
        binding.cardBreakfast.setOnClickListener {
            currentMealType = MealType.BREAKFAST
            setupFoodSelection(breakfastOptions)
        }
        binding.cardLunch.setOnClickListener {
            currentMealType = MealType.LUNCH
            setupFoodSelection(lunchOptions)
        }
        binding.cardDinner.setOnClickListener {
            currentMealType = MealType.DINNER
            setupFoodSelection(dinnerOptions)
        }
        binding.btnViewRecipe.setOnClickListener {
            selectedFoodItem?.let {
                val recipeSheet = RecipeBottomSheetFragment.newInstance(it.name, it.recipeDetails)
                recipeSheet.show(childFragmentManager, RecipeBottomSheetFragment.TAG)
            }
        }
        binding.btnViewVideo.setOnClickListener {
            selectedFoodItem?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.videoUrl))
                startActivity(intent)
            }
        }
        binding.btnSetReminders.setOnClickListener {
            findNavController().navigate(R.id.action_mealPlanFragment_to_mealRemindersFragment)
        }
        binding.toolbar.setNavigationOnClickListener {
            if (binding.cardCookingTutorial.visibility == View.GONE) {
                updateRightPanel(RightPanelState.TUTORIAL)
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun showDatePickerDialog() {
        val today = selectedDate?.calendar ?: Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val newSelectedCalendar = Calendar.getInstance()
                newSelectedCalendar.set(year, month, dayOfMonth)
                updateCalendarSelection(newSelectedCalendar)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateCalendarSelection(newDate: Calendar) {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_YEAR, -daysInPast)

        val diff = newDate.timeInMillis - startDate.timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(diff).toInt()

        if (days >= 0 && days < calendarDates.size) {
            // Deselect old date
            val oldIndex = calendarDates.indexOfFirst { it.isSelected }
            if (oldIndex != -1) {
                calendarDates[oldIndex].isSelected = false
                dateAdapter.notifyItemChanged(oldIndex)
            }
            // Select new date
            calendarDates[days].isSelected = true
            selectedDate = calendarDates[days]
            dateAdapter.notifyItemChanged(days)
            binding.recyclerViewCalendar.scrollToPosition(days)

            loadMealPlanForDate(calendarDates[days])
            updateDateTitle(calendarDates[days])
        }
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        val totalDays = 730 // Two years total

        // Move calendar to the start date (one year ago)
        calendar.add(Calendar.DAY_OF_YEAR, -daysInPast)

        for (i in 0..totalDays) {
            val isToday = i == daysInPast
            calendarDates.add(CalendarDate(calendar.clone() as Calendar, isSelected = isToday))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        selectedDate = calendarDates.find { it.isSelected }

        dateAdapter = DateAdapter(calendarDates) { date ->
            selectedDate = date
            loadMealPlanForDate(date)
            updateDateTitle(date)
        }
        binding.recyclerViewCalendar.adapter = dateAdapter
        binding.recyclerViewCalendar.scrollToPosition(daysInPast)

        selectedDate?.let {
            loadMealPlanForDate(it)
            updateDateTitle(it)
        }
    }

    private fun updateDateTitle(date: CalendarDate) {
        val titleFormat: SimpleDateFormat
        val today = Calendar.getInstance()

        if (date.calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            date.calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            titleFormat = SimpleDateFormat("'Today' dd MMMM yyyy", Locale.getDefault())
        } else {
            titleFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        }
        binding.textDateTitle.text = titleFormat.format(date.calendar.time)
    }

    private fun loadMealPlanForDate(date: CalendarDate) {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.calendar.time)
        val plan = mealPlanHistory.getOrDefault(dateKey, DailyMealPlan(defaultBreakfast, defaultLunch, defaultDinner))

        updateMealCardUI(binding.imgBreakfast, binding.textBreakfastName, binding.textBreakfastCalories, plan.breakfast)
        updateMealCardUI(binding.imgLunch, binding.textLunchName, binding.textLunchCalories, plan.lunch)
        updateMealCardUI(binding.imgDinner, binding.textDinnerName, binding.textDinnerCalories, plan.dinner)
    }

    private fun onFoodItemSelected(foodItem: FoodItem) {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate!!.calendar.time)
        val currentPlan = mealPlanHistory.getOrDefault(dateKey, DailyMealPlan(null, null, null))

        when (currentMealType) {
            MealType.BREAKFAST -> {
                currentPlan.breakfast = foodItem
                updateMealCardUI(binding.imgBreakfast, binding.textBreakfastName, binding.textBreakfastCalories, foodItem)
            }
            MealType.LUNCH -> {
                currentPlan.lunch = foodItem
                updateMealCardUI(binding.imgLunch, binding.textLunchName, binding.textLunchCalories, foodItem)
            }
            MealType.DINNER -> {
                currentPlan.dinner = foodItem
                updateMealCardUI(binding.imgDinner, binding.textDinnerName, binding.textDinnerCalories, foodItem)
            }
        }

        mealPlanHistory[dateKey] = currentPlan

        selectedFoodItem = foodItem
        binding.imageRecipeHeader.setImageResource(foodItem.imageResId)
        binding.textRecipeName.text = foodItem.name

        updateRightPanel(RightPanelState.DETAIL)
    }

    private fun updateMealCardUI(imageView: ImageView, nameView: TextView, calView: TextView, foodItem: FoodItem?) {
        foodItem?.let {
            imageView.setImageResource(it.imageResId)
            nameView.text = it.name
            calView.text = if (it.calories > 0) "${it.calories}kcal" else ""
        }
    }

    private fun setupFoodSelection(foodList: List<FoodItem>) {
        val adapter = FoodGridAdapter(foodList) { foodItem ->
            onFoodItemSelected(foodItem)
        }
        binding.recyclerViewFoodGrid.adapter = adapter
        updateRightPanel(RightPanelState.SELECTION)
    }

    private fun updateRightPanel(newState: RightPanelState, animate: Boolean = true) {
        val newView = when (newState) {
            RightPanelState.TUTORIAL -> binding.cardCookingTutorial
            RightPanelState.SELECTION -> binding.recyclerViewFoodGrid
            RightPanelState.DETAIL -> binding.cardRecipeDetail
        }

        val currentVisibleView = listOf(binding.cardCookingTutorial, binding.recyclerViewFoodGrid, binding.cardRecipeDetail)
            .find { it.visibility == View.VISIBLE }

        if (currentVisibleView == newView) return

        val animationDuration = if (animate) 300L else 0L
        val slideDistance = binding.rightColumnContainer.width.toFloat()

        currentVisibleView?.animate()
            ?.translationX(-slideDistance)
            ?.alpha(0f)
            ?.setDuration(animationDuration)
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currentVisibleView.visibility = View.GONE
                    currentVisibleView.translationX = 0f
                    currentVisibleView.alpha = 1f
                }
            })

        newView.translationX = slideDistance
        newView.alpha = 0f
        newView.visibility = View.VISIBLE

        newView.animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(animationDuration)
            .setListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

