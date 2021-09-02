package com.example.habitstracker.viewholders

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.adapters.RecyclerViewAdapter

class MainViewHolder(
    view: View,
    private val onHabitListener: RecyclerViewAdapter.HabitOnClickListener
) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    private val titleContainer: LinearLayout = view.findViewById(R.id.habitNameContainer)
    private val title: TextView = view.findViewById(R.id.habitName)
    private val description: TextView = view.findViewById(R.id.habitAbout)
    private val priority: TextView = view.findViewById(R.id.habitPriority)
    private val habitCount: TextView = view.findViewById(R.id.habitCount)
    private val doBtn: ImageButton = view.findViewById(R.id.habitDoBtn)

    fun bind(view: Habit) {
        //sets color opacity on 25%
        val newColor = view.color and 0x00FFFFFF or 0x40000000
        titleContainer.backgroundTintList = ColorStateList.valueOf(newColor)
        title.text = view.title
        description.text = view.description
        priority.text = when (view.priority) {
            0 -> "Низкий"
            1 -> "Средний"
            else -> "Большой"
        }
        habitCount.text = view.count.toString()

        setDoBtnListener()
    }

    private fun setDoBtnListener() {
        doBtn.setOnClickListener {
            habitCount.text = (habitCount.text.toString().toInt() - 1).toString()
        }
    }

    override fun onClick(v: View?) {
        onHabitListener.onClick(adapterPosition)
    }
}