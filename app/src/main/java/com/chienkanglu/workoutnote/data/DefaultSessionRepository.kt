package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.PopulatedSession
import com.chienkanglu.workoutnote.data.model.asExternalModel
import com.chienkanglu.workoutnote.database.dao.SessionDao
import com.chienkanglu.workoutnote.database.model.PopulatedSessionEntity
import com.chienkanglu.workoutnote.database.model.SessionEntity
import com.chienkanglu.workoutnote.database.model.SessionExerciseCrossRef
import com.chienkanglu.workoutnote.database.model.SetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class DefaultSessionRepository
    @Inject
    constructor(
        private val sessionDao: SessionDao,
    ) : SessionRepository {
        override fun getSessions() = sessionDao.getSessions().map { it.map(PopulatedSessionEntity::asExternalModel) }

        override fun getSession(id: Int): Flow<PopulatedSession> = sessionDao.getSession(id).map { it.asExternalModel() }

        override suspend fun insertSession() =
            sessionDao.insertSession(
                SessionEntity(date = Clock.System.now()),
            ) != -1L

        override suspend fun deleteSessions(ids: List<Int>) = sessionDao.deleteSessions(ids) == ids.size

        override suspend fun insertExerciseToSession(
            sessionId: Int,
            exerciseId: Int,
        ) = sessionDao.insertSessionExerciseCrossRef(
            SessionExerciseCrossRef(
                sessionId = sessionId,
                exerciseId = exerciseId,
            ),
        ) != -1L

        override suspend fun deleteExercisesFromSession(
            sessionId: Int,
            exerciseIds: List<Int>,
        ) = sessionDao.deleteExercisesFromSession(
            sessionId = sessionId,
            exerciseIds = exerciseIds,
        ) == exerciseIds.size

        override suspend fun insertSetToExercise(
            sessionId: Int,
            exerciseId: Int,
            reps: Int,
            weight: Double,
        ) = sessionDao.insertSetToExercise(
            SetEntity(
                sessionId = sessionId,
                exerciseId = exerciseId,
                reps = reps,
                weight = weight,
            ),
        ) != -1L

        override suspend fun deleteSetFromExercise(setIds: List<Int>) =
            sessionDao.deleteSetFromExercise(
                setIds,
            ) == setIds.size
    }
