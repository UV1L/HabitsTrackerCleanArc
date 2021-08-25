package com.example.domain.entities

data class Habit(
    val name: String,
    val about: String,
    val type: String,
    val priority: String,
    val color: Int
) {

    override fun equals(other: Any?): Boolean {
        val habit = other as? Habit ?: return false

        return habit.name == this.name &&
                habit.about == this.about &&
                habit.type == this.type &&
                habit.priority == this.priority &&
                habit.color == this.color
    }
}