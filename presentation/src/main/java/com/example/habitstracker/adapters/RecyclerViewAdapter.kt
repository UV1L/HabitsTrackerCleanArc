package com.example.habitstracker.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.habitstracker.R
import com.example.habitstracker.viewholders.MainViewHolder
import javax.security.auth.callback.Callback

class RecyclerViewAdapter(private val onHabitListener: HabitOnClickListener) :
    ListAdapter<Habit, MainViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habit, parent, false)

        return MainViewHolder(view, onHabitListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun addElements(habits: List<Habit>) {
        submitList(ArrayList(currentList).apply { addAll(habits) })
    }

    interface HabitOnClickListener {

        fun onClick(habitPosition: Int)
    }
}

class ItemCallback : DiffUtil.ItemCallback<Habit>() {

    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }
}