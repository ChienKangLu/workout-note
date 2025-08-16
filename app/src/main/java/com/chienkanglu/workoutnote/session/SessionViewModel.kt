package com.chienkanglu.workoutnote.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chienkanglu.workoutnote.data.DefaultExerciseRepository
import com.chienkanglu.workoutnote.data.DefaultSessionRepository
import com.chienkanglu.workoutnote.data.model.Exercise
import com.chienkanglu.workoutnote.data.model.PopulatedSession
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SessionViewModel.Factory::class)
class SessionViewModel
    @AssistedInject
    constructor(
        private val sessionRepository: DefaultSessionRepository,
        exerciseRepository: DefaultExerciseRepository,
        @Assisted val sessionId: Int,
    ) : ViewModel() {
        val sessionUiState: StateFlow<SessionUiState> =
            sessionRepository
                .getSession(sessionId)
                .map<PopulatedSession, SessionUiState>(SessionUiState::Success)
                .onStart { emit(SessionUiState.Loading) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SessionUiState.Loading,
                )

        val exercisesUiState: StateFlow<ExercisesUiState> =
            exerciseRepository
                .getExercises()
                .map<List<Exercise>, ExercisesUiState>(ExercisesUiState::Success)
                .onStart { emit(ExercisesUiState.Loading) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ExercisesUiState.Loading,
                )

        fun addExerciseToSession(exercise: Exercise) {
            viewModelScope.launch {
                sessionRepository.insertExerciseToSession(sessionId, exercise.id)
            }
        }

        fun deleteExerciseFromSession(exerciseId: Int) {
            viewModelScope.launch {
                sessionRepository.deleteExercisesFromSession(sessionId, listOf(exerciseId))
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(sessionId: Int): SessionViewModel
        }
    }
