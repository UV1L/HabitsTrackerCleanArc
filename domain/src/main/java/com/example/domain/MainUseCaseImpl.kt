package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

class MainUseCaseImpl(private val repository: MainRepository) : MainUseCase {

    override fun start(): Flow<List<Habit>> {
        return repository.getHabits()
    }
}