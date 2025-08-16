package com.chienkanglu.workoutnote.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * External data layer representation of a fully populated session
 */
data class PopulatedSessionEntity(
    @Embedded val session: SessionEntity,
    @Relation(
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
