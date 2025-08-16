package com.chienkanglu.workoutnote.session

import com.chienkanglu.workoutnote.data.model.PopulatedSession

sealed interface SessionUiState {
    data object Loading : SessionUiState

    data class Success(
        val session: PopulatedSession,
    ) : SessionUiState
}
