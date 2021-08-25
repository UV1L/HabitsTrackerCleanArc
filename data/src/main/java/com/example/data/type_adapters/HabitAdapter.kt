package com.example.data.type_adapters

import com.example.domain.entities.Habit
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*

class HabitAdapter : TypeAdapter<Habit>() {

    override fun write(out: JsonWriter?, value: Habit?) {

        out?.apply {
            beginObject()

            value?.let {
                name("title").value(
                    when(it.name) {
                        null -> "empty"
                        else -> it.name
                    }
                )
            }

            endObject()
        }
    }

    override fun read(`in`: JsonReader?): Habit {
        var title: String? = null

        `in`?.beginObject()
        while (`in`?.hasNext() == true) {
            val name = `in`.nextName()

            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                continue
            }

            when (name) {
                "title" -> title = `in`.nextString()
            }
        }
        `in`?.endObject()

        return when(title) {
            null -> Habit("empty", "empty", "empty", "empty", 0)
            else -> Habit(title, "empty", "empty", "empty", 0)
        }
    }
}