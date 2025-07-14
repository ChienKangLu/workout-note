package com.chienkanglu.workoutnote.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.data.model.Session
import com.chienkanglu.workoutnote.ui.common.LocalTimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
internal fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val sessionUiState by viewModel.sessionUiState.collectAsStateWithLifecycle()
    HomeScreen(
        sessionUiState = sessionUiState,
        onAddButtonClick = { viewModel.insertSession() },
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    sessionUiState: SessionUiState,
    onAddButtonClick: () -> Unit,
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
            IconButton(onAddButtonClick) {
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

        when (sessionUiState) {
            is SessionUiState.Loading -> Unit
            is SessionUiState.Success ->
                if (sessionUiState.sessions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.home_no_sessions),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                } else {
                    LazyColumn {
                        itemsIndexed(items = sessionUiState.sessions) { index, session ->
                            SessionItem(
                                session,
                                index == 0,
                                modifier,
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
    modifier: Modifier = Modifier,
) {
    if (!isFirst) {
        HorizontalDivider(thickness = 1.dp)
    }

    Row(
        modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
    ) {
        Text(
            text = dateFormatted(session.date),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun dateFormatted(publishDate: Instant): String =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
        .withZone(LocalTimeZone.current.toJavaZoneId())
        .format(publishDate.toJavaInstant())
