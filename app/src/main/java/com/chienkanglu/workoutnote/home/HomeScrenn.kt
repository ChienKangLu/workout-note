package com.chienkanglu.workoutnote.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.data.model.Session
import com.chienkanglu.workoutnote.ui.common.ActionSheet
import com.chienkanglu.workoutnote.ui.common.EmptySection
import com.chienkanglu.workoutnote.ui.common.dateFormatted

@Composable
internal fun HomeScreenRoute(
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val sessionsUiState by viewModel.sessionsUiState.collectAsStateWithLifecycle()
    HomeScreen(
        sessionsUiState = sessionsUiState,
        addSession = { viewModel.insertSession() },
        deleteSessions = { ids ->
            viewModel.deleteSessions(ids)
        },
        onSessionClick = onSessionClick,
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    sessionsUiState: SessionsUiState,
    addSession: () -> Unit,
    deleteSessions: (List<Int>) -> Unit,
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(16.dp),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(addSession) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }

        Text(
            stringResource(R.string.home),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = modifier.height(10.dp))

        when (sessionsUiState) {
            is SessionsUiState.Loading -> Unit
            is SessionsUiState.Success ->
                if (sessionsUiState.sessions.isEmpty()) {
                    EmptySection(text = stringResource(R.string.no_sessions))
                } else {
                    LazyColumn {
                        itemsIndexed(items = sessionsUiState.sessions) { index, session ->
                            SessionItem(
                                session = session.session,
                                isFirst = index == 0,
                                deleteSessions = deleteSessions,
                                onSessionClick = onSessionClick,
                                modifier = modifier,
                            )
                        }
                    }
                }
        }
    }
}

@Composable
fun SessionItem(
    session: Session,
    isFirst: Boolean,
    deleteSessions: (List<Int>) -> Unit,
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isFirst) {
        HorizontalDivider(thickness = 1.dp)
    }

    var showActionSheet by rememberSaveable { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    Row(
        modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onSessionClick(session.id)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    showActionSheet = true
                },
            ).padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
    ) {
        Text(
            text = dateFormatted(session.date),
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (showActionSheet) {
        ActionSheet(
            onDismissSheet = { showActionSheet = false },
            onDelete = {
                deleteSessions(listOf(session.id))
            },
        )
    }
}
