package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.db.entities.HabitEntity
import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM HabitEntity")
    fun getAll(): Flow<List<Habit>>

    @Insert
    fun insert(habit: HabitEntity)

    @Update
    fun update(habit: HabitEntity)

    @Query("SELECT * FROM HabitEntity WHERE title == :title")
    fun findByTitle(title: String): HabitEntity
}