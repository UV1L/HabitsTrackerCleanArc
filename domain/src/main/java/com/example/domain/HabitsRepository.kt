package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    val data: Flow<List<Habit>>

    suspend fun addHabit(habit: Habit)

    suspend fun findHabitId(habit: Habit): Int

    suspend fun updateHabit(oldHabit: Habit, newHabit: Habit)
}