package com.example.data

import com.example.data.db.HabitDb
import com.example.data.db.entities.HabitEntityMapper
import com.example.domain.entities.Habit
import com.example.domain.HabitsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.coroutineContext

class HabitsRepositoryImpl(private val retrofitService: RetrofitService,
                           habitDb: HabitDb
) : HabitsRepository {

    private val habitDao = habitDb.habitDao()

    override val data: Flow<List<Habit>>
    get() = habitDao.getAll()

    override suspend fun addHabit(habit: Habit) {

        withContext(Dispatchers.IO) {

            val habitEntity = HabitEntityMapper.getHabitEntity(habit)
            habitDao.insert(habitEntity)
        }
    }

    override suspend fun findHabitId(habit: Habit): Int {
        return habitDao.findByTitle(habit.title).uid!!
    }

    override suspend fun updateHabit(oldHabit: Habit, newHabit: Habit) {

        withContext(Dispatchers.IO) {

            val uid = findHabitId(oldHabit)
            val habitEntity = HabitEntityMapper.getHabitEntity(newHabit, uid)

            habitDao.update(habitEntity)
        }
    }
}