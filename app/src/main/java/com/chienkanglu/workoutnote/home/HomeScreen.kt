package com.chienkanglu.workoutnote.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.data.model.PopulatedSession
import com.chienkanglu.workoutnote.ui.common.ActionSheet
import com.chienkanglu.workoutnote.ui.common.DateTimePattern
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
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        when (sessionsUiState) {
            is SessionsUiState.Loading -> Unit
            is SessionsUiState.Success ->
                if (sessionsUiState.sessions.isEmpty()) {
                    EmptySection(text = stringResource(R.string.no_sessions))
                } else {
                    LazyColumn {
                        itemsIndexed(items = sessionsUiState.sessions) { index, session ->
                            SessionItem(
                                populatedSession = session,
                                isFirst = index == 0,
                                deleteSessions = deleteSessions,
                                onSessionClick = onSessionClick,
                            )
                        }
                    }
                }
        }
    }
}

@Composable
fun SessionItem(
    populatedSession: PopulatedSession,
    isFirst: Boolean,
    deleteSessions: (List<Int>) -> Unit,
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val session = populatedSession.session

    if (!isFirst) {
        HorizontalDivider(thickness = 1.dp)
    }

    var showActionSheet by rememberSaveable { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    Column(
        modifier =
            modifier
                .combinedClickable(
                    onClick = {
                        onSessionClick(session.id)
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        showActionSheet = true
                    },
                ).padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
    ) {
        Text(
            text = dateFormatted(session.date),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier.height(8.dp))
        SessionDetailList(populatedSession = populatedSession)
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

@Composable
fun SessionDetailList(
    populatedSession: PopulatedSession,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        SessionDetailListItem(
            imageVector = Icons.Filled.CalendarMonth,
            text = dateFormatted(populatedSession.session.date, DateTimePattern.DateOnly),
        )
        SessionDetailListItem(
            imageVector = Icons.Filled.AccessTimeFilled,
            text = dateFormatted(populatedSession.session.date, DateTimePattern.TimeOnly),
        )
        SessionDetailListItem(
            imageVector = Icons.Filled.FitnessCenter,
            text = populatedSession.exercises.size.toString(),
        )
    }
}

@Composable
fun SessionDetailListItem(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier =
                Modifier
                    .padding(end = 4.dp)
                    .size(16.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier.width(8.dp))
    }
}
