package com.example.habitstracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.domain.entities.Habit
import com.example.domain.MainUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val mainUseCase: MainUseCase) : ViewModel() {

    private val _habits: MutableLiveData<List<Habit>> = MutableLiveData()
    val habits: LiveData<List<Habit>> = _habits

    fun load() {
        _habits.value = mainUseCase.start().asLiveData().value
    }

    suspend fun addHabit(habit: Habit) {

        withContext(currentCoroutineContext()) {

            val currentValue = _habits.value
            _habits.postValue(
                currentValue?.plus(habit) ?: listOf(habit)
            )
        }
    }
}