package com.chienkanglu.workoutnote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chienkanglu.workoutnote.database.dao.ExerciseDao
import com.chienkanglu.workoutnote.database.dao.SessionDao
import com.chienkanglu.workoutnote.database.model.ExerciseEntity
import com.chienkanglu.workoutnote.database.model.SessionEntity
import com.chienkanglu.workoutnote.database.util.InstantConverter

@Database(
    entities = [
        SessionEntity::class,
        ExerciseEntity::class,
    ],
    version = 1,
)
@TypeConverters(InstantConverter::class)
abstract class WntDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    abstract fun exerciseDao(): ExerciseDao
}
