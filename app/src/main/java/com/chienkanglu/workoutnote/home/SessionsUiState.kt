package com.chienkanglu.workoutnote.home

import com.chienkanglu.workoutnote.data.model.PopulatedSession

sealed interface SessionsUiState {
    data object Loading : SessionsUiState

    data class Success(
        val sessions: List<PopulatedSession> = emptyList(),
    ) : SessionsUiState
}
