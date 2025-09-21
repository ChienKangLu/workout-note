package com.chienkanglu.workoutnote.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithSetsEntity(
    @Embedded val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exercise_id",
    )
    val sets: List<SetEntity>,
)
