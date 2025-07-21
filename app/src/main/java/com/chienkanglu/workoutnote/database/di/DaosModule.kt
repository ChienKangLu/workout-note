package com.chienkanglu.workoutnote.database.di

import com.chienkanglu.workoutnote.database.WntDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesSessionDao(database: WntDatabase) = database.sessionDao()

    @Provides
    fun providesExerciseDao(database: WntDatabase) = database.exerciseDao()
}
