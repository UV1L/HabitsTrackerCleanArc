package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    fun start(): Flow<List<Habit>>
}