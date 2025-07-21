package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.asExternalModel
import com.chienkanglu.workoutnote.database.dao.SessionDao
import com.chienkanglu.workoutnote.database.model.SessionEntity
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class DefaultSessionRepository
    @Inject
    constructor(
        private val sessionDao: SessionDao,
    ) : SessionRepository {
        override fun getSessions() = sessionDao.getSessions().map { it.map(SessionEntity::asExternalModel) }

        override suspend fun insertSession() =
            sessionDao.insertSession(
                SessionEntity(date = Clock.System.now()),
            ) != -1L

        override suspend fun deleteSessions(ids: List<Int>) = sessionDao.deleteSessions(ids) == ids.size
    }
