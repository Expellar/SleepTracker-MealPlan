package com.example.sleeptrackermealplan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptrackermealplan.data.DailyReview
import com.example.sleeptrackermealplan.data.ReviewStatus
import java.text.SimpleDateFormat
import java.util.Locale

class WeeklyReviewAdapter(
    private val dates: List<DailyReview>,
    private val onItemClicked: (DailyReview, Int) -> Unit
) : RecyclerView.Adapter<WeeklyReviewAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date_review, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
        holder.itemView.setOnClickListener {
            onItemClicked(date, position)
        }
    }

    override fun getItemCount(): Int = dates.size

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayOfWeekText: TextView = itemView.findViewById(R.id.text_day_of_week)
        private val dateText: TextView = itemView.findViewById(R.id.text_date)
        private val statusDot: View = itemView.findViewById(R.id.status_dot)
        private val container: View = itemView.findViewById(R.id.date_item_container)

        fun bind(dailyReview: DailyReview) {
            val dayFormat = SimpleDateFormat("E", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

            dayOfWeekText.text = dayFormat.format(dailyReview.calendar.time)
            dateText.text = dateFormat.format(dailyReview.calendar.time)

            // --- THIS IS THE NEW LOGIC TO CHANGE APPEARANCE ---
            if (dailyReview.isSelected) {
                container.setBackgroundResource(R.drawable.bg_date_selected)
                dayOfWeekText.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                dateText.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
            } else {
                container.setBackgroundResource(R.drawable.bg_date_unselected)
                dayOfWeekText.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                dateText.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            }
            // --- END OF NEW LOGIC ---

            val dotDrawable = when (dailyReview.status) {
                ReviewStatus.GOOD -> R.drawable.dot_good
                ReviewStatus.OKAY -> R.drawable.dot_okay
                ReviewStatus.BAD -> R.drawable.dot_bad
            }
            statusDot.setBackgroundResource(dotDrawable)
        }
    }
}

