package com.chienkanglu.workoutnote.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SessionWithExercisesEntity(
    @Embedded val session: SessionEntity,
    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = SessionExerciseCrossRef::class,
                parentColumn = "session_id",
                entityColumn = "exercise_id",
            ),
    )
    val exercises: List<ExerciseEntity>,
)
