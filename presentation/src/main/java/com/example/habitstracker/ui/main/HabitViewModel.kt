package com.example.habitstracker.ui.main

import androidx.lifecycle.*
import com.example.domain.entities.Habit
import com.example.domain.AddHabitsUseCase
import kotlinx.coroutines.*

class HabitViewModel(private val addHabitsUseCase: AddHabitsUseCase) : ViewModel() {

    val habits: LiveData<List<Habit>> = addHabitsUseCase.data.asLiveData()

    fun load(habit: Habit) {

        viewModelScope.launch {

            addHabitsUseCase.execute(habit)
        }
    }

    fun updateHabit(oldHabit: Habit, newHabit: Habit) {

        viewModelScope.launch {

            addHabitsUseCase.update(oldHabit, newHabit)
        }
    }

    @Deprecated("пока не нужно")
    suspend fun addHabit(habit: Habit) {

        withContext(currentCoroutineContext()) {

//            val currentValue = _habits.value
//            _habits.postValue(
//                currentValue?.plus(habit) ?: listOf(habit)
//            )
        }
    }
}