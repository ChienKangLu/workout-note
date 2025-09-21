package com.chienkanglu.workoutnote.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "set",
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
        ForeignKey(
            entity = SessionExerciseCrossRef::class,
            parentColumns = ["session_id", "exercise_id"],
            childColumns = ["session_id", "exercise_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "session_id")
    val sessionId: Int,
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
    val reps: Int,
    val weight: Double,
)
