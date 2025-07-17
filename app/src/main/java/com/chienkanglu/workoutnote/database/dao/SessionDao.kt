package com.chienkanglu.workoutnote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chienkanglu.workoutnote.database.model.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: SessionEntity): Long

    @Query("SELECT * FROM session")
    fun getSessions(): Flow<List<SessionEntity>>

    @Query(
        value = """
            DELETE FROM session
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteSessions(ids: List<Int>): Int
}
