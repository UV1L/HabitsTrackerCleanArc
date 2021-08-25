package com.example.data

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitService {

    @GET("/habits")
    fun getHabits(@Header("Authorization") token: String): Flow<List<Habit>>
}