package com.example.habitstracker.fabric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.MainUseCase
import com.example.domain.MainUseCaseImpl
import com.example.habitstracker.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val useCase: MainUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            mainUseCase = useCase,
        ) as T
    }
}