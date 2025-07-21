package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.asExternalModel
import com.chienkanglu.workoutnote.database.dao.ExerciseDao
import com.chienkanglu.workoutnote.database.model.ExerciseEntity
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultExerciseRepository
    @Inject
    constructor(
        private val exerciseDao: ExerciseDao,
    ) : ExerciseRepository {
        override fun getExercises() = exerciseDao.getExercises().map { it.map(ExerciseEntity::asExternalModel) }

        override suspend fun insertExercise(name: String) =
            exerciseDao.insertExercise(
                ExerciseEntity(name = name),
            ) != -1L

        override suspend fun deleteExercises(ids: List<Int>) = exerciseDao.deleteExercises(ids) == ids.size
    }
