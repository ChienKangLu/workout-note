package com.chienkanglu.workoutnote.data.di

import com.chienkanglu.workoutnote.data.DefaultSessionRepository
import com.chienkanglu.workoutnote.data.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsSessionRepository(sessionRepository: DefaultSessionRepository): SessionRepository
}
