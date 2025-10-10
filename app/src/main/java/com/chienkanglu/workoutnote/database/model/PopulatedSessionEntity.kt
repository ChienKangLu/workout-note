package com.chienkanglu.workoutnote.database.model

/**
 * External data layer representation of a fully populated session
 *
 * As Room does not support multiple columns with @Relation, the related data must be populated manually.
 * https://issuetracker.google.com/issues/64247765
 */
data class PopulatedSessionEntity(
    val session: SessionEntity,
    val exercises: List<ExerciseWithSetsEntity>,
)

data class ExerciseWithSetsEntity(
    val exercise: ExerciseEntity,
    val sets: List<SetEntity>,
)
