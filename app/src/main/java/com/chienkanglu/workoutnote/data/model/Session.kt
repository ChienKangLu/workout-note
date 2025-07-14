package com.chienkanglu.workoutnote.data.model

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
