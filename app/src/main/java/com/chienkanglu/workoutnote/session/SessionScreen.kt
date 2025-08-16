@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.chienkanglu.workoutnote.session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.data.model.Exercise
import com.chienkanglu.workoutnote.ui.common.EmptySection
import com.chienkanglu.workoutnote.ui.common.dateFormatted
import com.chienkanglu.workoutnote.ui.common.isScrollingUp

@Composable
internal fun SessionScreenRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SessionViewModel = hiltViewModel(),
) {
    val sessionUiState by viewModel.sessionUiState.collectAsStateWithLifecycle()
    val exercisesUiState by viewModel.exercisesUiState.collectAsStateWithLifecycle()

    SessionScreen(
        sessionUiState = sessionUiState,
        exercisesUiState = exercisesUiState,
        onBackClick = onBackClick,
        addExerciseToSession = { exercise -> viewModel.addExerciseToSession(exercise) },
        deleteExerciseFromSession = { exerciseId -> viewModel.deleteExerciseFromSession(exerciseId) },
        modifier = modifier,
    )
}

@Composable
fun SessionScreen(
    sessionUiState: SessionUiState,
    exercisesUiState: ExercisesUiState,
    onBackClick: () -> Unit,
    addExerciseToSession: (Exercise) -> Unit,
    deleteExerciseFromSession: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    var showExerciseDialog by remember { mutableStateOf(false) }

    val title =
        when (sessionUiState) {
            is SessionUiState.Loading -> ""
            is SessionUiState.Success -> dateFormatted(sessionUiState.session.session.date)
        }

    val availableExercises =
        when (exercisesUiState) {
            is ExercisesUiState.Loading -> emptyList()
            is ExercisesUiState.Success -> exercisesUiState.exercises
        }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showExerciseDialog = true },
                expanded = listState.isScrollingUp(),
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text(text = stringResource(R.string.new_exercise)) },
            )
        },
    ) { innerPadding ->
        when (sessionUiState) {
            SessionUiState.Loading -> Unit
            is SessionUiState.Success -> {
                val exercises = sessionUiState.session.exercises

                if (exercises.isEmpty()) {
                    EmptySection(text = stringResource(R.string.no_exercises))
                } else {
                    LazyColumn(
                        state = listState,
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(count = exercises.size) {
                            val exercise = exercises[it]

                            ExerciseItem(
                                name = exercise.name,
                                deleteExerciseFromSession = {
                                    deleteExerciseFromSession(exercise.id)
                                },
                                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    if (showExerciseDialog) {
        ExerciseDialog(
            exercises = availableExercises,
            onDismissRequest = { showExerciseDialog = false },
            addExerciseToSession = addExerciseToSession,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
fun ExerciseItem(
    name: String,
    deleteExerciseFromSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
        )
        Box {
            IconButton(onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = null,
                )
            }
            ExerciseItemMenu(
                dropDownMenuExpanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
                deleteExerciseFromSession = deleteExerciseFromSession,
            )
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun ExerciseItemMenu(
    dropDownMenuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    deleteExerciseFromSession: () -> Unit,
) {
    DropdownMenu(
        expanded = dropDownMenuExpanded,
        onDismissRequest = onDismissRequest,
    ) {
        HorizontalDivider()

        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.delete),
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                )
            },
            onClick = {
                onDismissRequest()
                deleteExerciseFromSession()
            },
        )
    }
}

@Composable
fun ExerciseDialog(
    exercises: List<Exercise>,
    onDismissRequest: () -> Unit,
    addExerciseToSession: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn {
                items(count = exercises.size) { index ->
                    val exercise = exercises[index]

                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier =
                            Modifier
                                .clickable {
                                    onDismissRequest()
                                    addExerciseToSession(exercise)
                                }.padding(16.dp)
                                .fillMaxWidth(),
                    )
                }
            }
        }
    }
}
