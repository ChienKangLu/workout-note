package com.chienkanglu.workoutnote.exercises

import com.chienkanglu.workoutnote.data.model.Exercise

sealed interface ExercisesUiState {
    data object Loading : ExercisesUiState

    data class Success(
        val exercises: List<Exercise> = emptyList(),
    ) : ExercisesUiState
}
