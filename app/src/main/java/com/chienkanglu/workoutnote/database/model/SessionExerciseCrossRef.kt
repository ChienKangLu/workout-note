package com.chienkanglu.workoutnote.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Cross reference for many to many relationship between [SessionEntity] and [ExerciseEntity]
 */
@Entity(
    tableName = "sessions_exercises",
    primaryKeys = ["session_id", "exercise_id"],
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["session_id"]),
        Index(value = ["exercise_id"]),
    ],
)
data class SessionExerciseCrossRef(
    @ColumnInfo(name = "session_id")
    val sessionId: Int,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
)
