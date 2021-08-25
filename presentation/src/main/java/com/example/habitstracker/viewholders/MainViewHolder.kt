package com.example.habitstracker.viewholders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.habitstracker.R

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val container: ConstraintLayout = view.findViewById(R.id.habitConstraintLayout)
    private val name: TextView = view.findViewById(R.id.habitName)
    private val about: TextView = view.findViewById(R.id.habitAbout)
    private val priority: TextView = view.findViewById(R.id.habitPriority)

    fun bind(view: Habit) {
        //sets color opacity on 25%
        val newColor = view.color and 0x00FFFFFF or 0x40000000
        container.setBackgroundColor(newColor)
        name.text = view.name
        about.text = view.about
        priority.text = view.priority.toLowerCase()
    }
}