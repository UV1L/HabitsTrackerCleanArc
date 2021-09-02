package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.entities.HabitEntity

@Database(entities = [HabitEntity::class], version = 2)
abstract class HabitDb : RoomDatabase() {

    companion object {
        const val NAME = "HabitDb"
    }

    abstract fun habitDao(): HabitsDao
}