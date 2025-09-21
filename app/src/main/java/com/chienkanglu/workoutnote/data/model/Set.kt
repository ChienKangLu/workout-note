package com.chienkanglu.workoutnote.data.model

import com.chienkanglu.workoutnote.database.model.SetEntity

data class Set(
    val id: Int = 0,
    val reps: Int,
    val weight: Double,
)

fun SetEntity.asExternalModel() =
    Set(
        id = id,
        reps = reps,
        weight = weight,
    )
