package com.chienkanglu.workoutnote.data.model

import com.chienkanglu.workoutnote.database.model.ExerciseEntity
import com.chienkanglu.workoutnote.database.model.ExerciseWithSetsEntity

data class Exercise(
    val id: Int = 0,
    val name: String,
)

fun ExerciseEntity.asExternalModel() =
    Exercise(
        id = id,
        name = name,
    )

data class PopulatedExercise(
    val exercise: Exercise,
    val sets: List<Set>,
)

fun ExerciseWithSetsEntity.asExternalModel() =
    PopulatedExercise(
        exercise = exercise.asExternalModel(),
        sets = sets.map { it.asExternalModel() },
    )
