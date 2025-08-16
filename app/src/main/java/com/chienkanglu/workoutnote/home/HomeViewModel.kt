package com.chienkanglu.workoutnote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chienkanglu.workoutnote.data.DefaultSessionRepository
import com.chienkanglu.workoutnote.data.model.PopulatedSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class
HomeViewModel
    @Inject
    constructor(
        private val sessionRepository: DefaultSessionRepository,
    ) : ViewModel() {
        val sessionsUiState: StateFlow<SessionsUiState> =
            sessionRepository
                .getSessions()
                .map<List<PopulatedSession>, SessionsUiState>(SessionsUiState::Success)
                .onStart { emit(SessionsUiState.Loading) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SessionsUiState.Loading,
                )

        fun insertSession() {
            viewModelScope.launch {
                sessionRepository.insertSession()
            }
        }

        fun deleteSessions(ids: List<Int>) {
            viewModelScope.launch {
                sessionRepository.deleteSessions(ids)
            }
        }
    }
