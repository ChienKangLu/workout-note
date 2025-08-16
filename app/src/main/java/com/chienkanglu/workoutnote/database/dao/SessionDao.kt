package com.chienkanglu.workoutnote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chienkanglu.workoutnote.database.model.PopulatedSessionEntity
import com.chienkanglu.workoutnote.database.model.SessionEntity
import com.chienkanglu.workoutnote.database.model.SessionExerciseCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: SessionEntity): Long

    @Transaction
    @Query("SELECT * FROM session")
    fun getSessions(): Flow<List<PopulatedSessionEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM session
            WHERE id = :id
        """,
    )
    fun getSession(id: Int): Flow<PopulatedSessionEntity>

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
}
