package com.chienkanglu.workoutnote.home

import com.chienkanglu.workoutnote.data.model.Session

sealed interface SessionUiState {
    data object Loading : SessionUiState

    data class Success(
        val sessions: List<Session> = emptyList(),
    ) : SessionUiState
}
