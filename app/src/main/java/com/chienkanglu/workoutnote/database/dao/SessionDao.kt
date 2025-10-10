package com.chienkanglu.workoutnote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chienkanglu.workoutnote.database.model.ExerciseWithSetsEntity
import com.chienkanglu.workoutnote.database.model.PopulatedSessionEntity
import com.chienkanglu.workoutnote.database.model.SessionEntity
import com.chienkanglu.workoutnote.database.model.SessionExerciseCrossRef
import com.chienkanglu.workoutnote.database.model.SessionWithExercisesEntity
import com.chienkanglu.workoutnote.database.model.SetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: SessionEntity): Long

    fun getSessions(): Flow<List<PopulatedSessionEntity>> =
        getSessionsWithExercises().combine(getSets()) { sessionsWithExercises, sets ->
            composePopulatedSessions(sessionsWithExercises, sets)
        }

    fun getSession(id: Int): Flow<PopulatedSessionEntity> =
        getSessionWithExercises(id).combine(getSets(id)) { sessionWithExercises, sets ->
            composePopulatedSessions(listOf(sessionWithExercises), sets).first()
        }

    private fun composePopulatedSessions(
        sessionsWithExercises: List<SessionWithExercisesEntity>,
        sets: List<SetEntity>,
    ) = sessionsWithExercises.map { sessionWithExercises ->
        PopulatedSessionEntity(
            session = sessionWithExercises.session,
            exercises =
                sessionWithExercises.exercises.map { exercise ->
                    val filteredSets =
                        sets.filter { it.sessionId == sessionWithExercises.session.id && it.exerciseId == exercise.id }
                    ExerciseWithSetsEntity(
                        exercise = exercise,
                        sets = filteredSets,
                    )
                },
        )
    }

    @Transaction
    @Query("SELECT * FROM session")
    fun getSessionsWithExercises(): Flow<List<SessionWithExercisesEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM session
            WHERE id = :id 
    """,
    )
    fun getSessionWithExercises(id: Int): Flow<SessionWithExercisesEntity>

    @Transaction
    @Query(
        value = """
            SELECT * FROM `set` 
        """,
    )
    fun getSets(): Flow<List<SetEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM `set` 
            WHERE session_id = :sessionId
        """,
    )
    fun getSets(sessionId: Int): Flow<List<SetEntity>>

    @Query(
        value = """
            DELETE FROM session
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteSessions(ids: List<Int>): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessionExerciseCrossRef(sessionExerciseCrossRef: SessionExerciseCrossRef): Long

    @Query(
        value = """
            DELETE FROM sessions_exercises
            WHERE session_id = :sessionId AND exercise_id IN (:exerciseIds)
        """,
    )
    suspend fun deleteExercisesFromSession(
        sessionId: Int,
        exerciseIds: List<Int>,
    ): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSetToExercise(setEntity: SetEntity): Long

    @Query(
        value = """
            DELETE FROM 'set'
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteSetFromExercise(ids: List<Int>): Int
}
