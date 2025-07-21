package com.chienkanglu.workoutnote.home

import com.chienkanglu.workoutnote.data.model.Session

sealed interface SessionsUiState {
    data object Loading : SessionsUiState

    data class Success(
        val sessions: List<Session> = emptyList(),
    ) : SessionsUiState
}
