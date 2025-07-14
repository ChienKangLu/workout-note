package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions(): Flow<List<Session>>

    suspend fun insertSession(): Boolean
}
