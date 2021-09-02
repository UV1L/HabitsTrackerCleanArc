package com.example.habitstracker.fabric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.AddHabitsUseCase
import com.example.habitstracker.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val useCase: AddHabitsUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            useCase,
        ) as T
    }
}