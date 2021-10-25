package com.example.habitstracker.fabric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.AddHabitsUseCase
import com.example.habitstracker.ui.main.HabitViewModel

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val useCase: AddHabitsUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HabitViewModel(
            useCase,
        ) as T
    }
}