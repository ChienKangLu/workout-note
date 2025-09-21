package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.PopulatedSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions(): Flow<List<PopulatedSession>>

    fun getSession(id: Int): Flow<PopulatedSession>

    suspend fun insertSession(): Boolean

    suspend fun deleteSessions(ids: List<Int>): Boolean

    suspend fun insertExerciseToSession(
        sessionId: Int,
        exerciseId: Int,
    ): Boolean

    suspend fun deleteExercisesFromSession(
        sessionId: Int,
        exerciseIds: List<Int>,
    ): Boolean

    suspend fun insertSetToExercise(
        sessionId: Int,
        exerciseId: Int,
        reps: Int,
        weight: Double,
    ): Boolean

    suspend fun deleteSetFromExercise(setIds: List<Int>): Boolean
}
