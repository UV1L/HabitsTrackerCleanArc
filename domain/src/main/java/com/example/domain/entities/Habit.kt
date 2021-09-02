package com.example.domain.entities

import java.io.Serializable

data class Habit(
    val title: String,
    val description: String,
    val type: Int,
    val priority: Int,
    val color: Int,
    val frequency: Int,
    val count: Int
) : Serializable {

    override fun equals(other: Any?): Boolean {
        val habit = other as? Habit ?: return false

        return habit.title == this.title &&
                habit.description == this.description &&
                habit.type == this.type &&
                habit.priority == this.priority &&
                habit.color == this.color &&
                habit.frequency == this.frequency &&
                habit.count == this.count
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + type
        result = 31 * result + priority
        result = 31 * result + color
        result = 31 * result + frequency
        result = 31 * result + count
        return result
    }
}