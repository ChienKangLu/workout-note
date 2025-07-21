package com.chienkanglu.workoutnote.data.model

import com.chienkanglu.workoutnote.database.model.ExerciseEntity

data class Exercise(
    val id: Int = 0,
    val name: String,
)

fun ExerciseEntity.asExternalModel() =
    Exercise(
        id = id,
        name = name,
    )
