package com.example.data

import com.example.domain.entities.Habit
import com.example.domain.MainRepository
import kotlinx.coroutines.flow.Flow

class MainRepositoryImpl(private val retrofitService: RetrofitService) : MainRepository {

    override fun getHabits(): Flow<List<Habit>> {
        return retrofitService.getHabits("")
    }
}