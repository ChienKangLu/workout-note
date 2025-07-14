package com.chienkanglu.workoutnote.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "session",
)
class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Instant,
)
