package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getHabits(): Flow<List<Habit>>
}