package com.example.domain

import com.example.domain.entities.Habit

class AddHabitsUseCaseImpl(private val repository: HabitsRepository) : AddHabitsUseCase {

    override val data = repository.data

    override suspend fun execute(habit: Habit) {
        repository.addHabit(habit)
    }

    override suspend fun update(oldHabit: Habit, newHabit: Habit) {
        repository.updateHabit(oldHabit, newHabit)
    }

    override suspend fun find(habit: Habit) {

    }
}