package com.example.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entities.Habit

@Entity
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int? ,
    @ColumnInfo(name = "color") val color: Int?,
    @ColumnInfo(name = "count") val count: Int?,
    @ColumnInfo(name = "date") val date: Int?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "frequency") val frequency: Int?,
    @ColumnInfo(name = "priority") val priority: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "type") val type: Int?,
)

class HabitEntityMapper {

    companion object {

        fun getHabitEntity(habit: Habit, uid: Int? = null) = HabitEntity(
            uid,
            habit.color,
            habit.count,
            0,
            habit.description,
            habit.frequency,
            habit.priority,
            habit.title,
            habit.type
        )
    }
}