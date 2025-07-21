package com.chienkanglu.workoutnote.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chienkanglu.workoutnote.data.DefaultExerciseRepository
import com.chienkanglu.workoutnote.data.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel
    @Inject
    constructor(
        private val exerciseRepository: DefaultExerciseRepository,
    ) : ViewModel() {
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

        fun insertExercise(name: String) {
            viewModelScope.launch {
                exerciseRepository.insertExercise(name)
            }
        }

        fun deleteExercises(ids: List<Int>) {
            viewModelScope.launch {
                exerciseRepository.deleteExercises(ids)
            }
        }
    }
