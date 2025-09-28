package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptrackermealplan.databinding.ItemDateBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Data class to hold the date and its selection state
// FIX: Using java.util.Calendar which is compatible with older Android versions
data class CalendarDate(val calendar: Calendar, var isSelected: Boolean = false)

class DateAdapter(
    private val dateList: MutableList<CalendarDate>,
    private val onDateClicked: (CalendarDate) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    class DateViewHolder(val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val calendarDate = dateList[position]

        // FIX: Using SimpleDateFormat for formatting
        val dayOfWeekFormat = SimpleDateFormat("E", Locale.getDefault())
        val dayOfMonthFormat = SimpleDateFormat("dd", Locale.getDefault())

        holder.binding.textDayOfWeek.text = dayOfWeekFormat.format(calendarDate.calendar.time)
        holder.binding.textDayOfMonth.text = dayOfMonthFormat.format(calendarDate.calendar.time)

        // Update the background and text color based on whether the date is selected
        if (calendarDate.isSelected) {
            holder.binding.dateContainer.setBackgroundResource(R.drawable.date_selected_background)
            holder.binding.textDayOfWeek.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.binding.textDayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        } else {
            holder.binding.dateContainer.setBackgroundResource(R.drawable.date_unselected_background)
            holder.binding.textDayOfWeek.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.binding.textDayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

        holder.itemView.setOnClickListener {
            // Find the previously selected item and deselect it
            val previouslySelectedItemIndex = dateList.indexOfFirst { it.isSelected }
            if (previouslySelectedItemIndex != -1) {
                dateList[previouslySelectedItemIndex].isSelected = false
                notifyItemChanged(previouslySelectedItemIndex)
            }

            // Select the new item
            calendarDate.isSelected = true
            notifyItemChanged(position)

            // Notify the fragment that a new date was clicked
            onDateClicked(calendarDate)
        }
    }

    override fun getItemCount() = dateList.size
}

