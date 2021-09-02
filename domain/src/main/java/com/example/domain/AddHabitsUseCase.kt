package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface AddHabitsUseCase {

    val data: Flow<List<Habit>>

    suspend fun execute(habit: Habit)

    suspend fun update(oldHabit: Habit, newHabit: Habit)

    suspend fun find(habit: Habit)
}