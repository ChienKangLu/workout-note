package com.chienkanglu.workoutnote.data.model

import com.chienkanglu.workoutnote.database.model.PopulatedSessionEntity
import com.chienkanglu.workoutnote.database.model.SessionEntity
import kotlinx.datetime.Instant

data class Session(
    val id: Int = 0,
    val date: Instant,
)

fun SessionEntity.asExternalModel() =
    Session(
        id = id,
        date = date,
    )

data class PopulatedSession(
    val session: Session,
    val exercises: List<PopulatedExercise>,
)

fun PopulatedSessionEntity.asExternalModel() =
    PopulatedSession(
        session = session.asExternalModel(),
        exercises = exercises.map { it.asExternalModel() },
    )
