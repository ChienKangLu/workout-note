package com.chienkanglu.workoutnote.database.di

import android.content.Context
import androidx.room.Room
import com.chienkanglu.workoutnote.database.WntDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): WntDatabase =
        Room
            .databaseBuilder(
                context,
                WntDatabase::class.java,
                "wnt-database",
            ).build()
}
