package com.chienkanglu.workoutnote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chienkanglu.workoutnote.database.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Query("SELECT * FROM exercise")
    fun getExercises(): Flow<List<ExerciseEntity>>

    @Query(
        value = """
            DELETE FROM exercise
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteExercises(ids: List<Int>): Int
}
