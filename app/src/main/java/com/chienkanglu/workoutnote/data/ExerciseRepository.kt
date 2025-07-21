package com.chienkanglu.workoutnote.data

import com.chienkanglu.workoutnote.data.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getExercises(): Flow<List<Exercise>>

    suspend fun insertExercise(name: String): Boolean

    suspend fun deleteExercises(ids: List<Int>): Boolean
}
