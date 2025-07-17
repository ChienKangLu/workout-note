package com.chienkanglu.workoutnote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chienkanglu.workoutnote.data.DefaultSessionRepository
import com.chienkanglu.workoutnote.data.model.Session
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
        val sessionUiState: StateFlow<SessionUiState> =
            sessionRepository
                .getSessions()
                .map<List<Session>, SessionUiState>(SessionUiState::Success)
                .onStart { emit(SessionUiState.Loading) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SessionUiState.Loading,
                )

        fun insertSession() {
            viewModelScope.launch {
                sessionRepository.insertSession()
            }
        }

        fun deleteSessions(ids: List<Int>) {
            viewModelScope.launch {
                sessionRepository.deleteSession(ids)
            }
        }
    }
