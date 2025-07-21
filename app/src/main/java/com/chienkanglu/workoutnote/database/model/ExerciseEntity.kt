package com.chienkanglu.workoutnote.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise",
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)
