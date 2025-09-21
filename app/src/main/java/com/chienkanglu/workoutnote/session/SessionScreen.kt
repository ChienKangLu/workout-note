@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.chienkanglu.workoutnote.session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chienkanglu.workoutnote.R
import com.chienkanglu.workoutnote.data.model.Exercise
import com.chienkanglu.workoutnote.data.model.PopulatedExercise
import com.chienkanglu.workoutnote.ui.common.ActionSheet
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
        addSetToExercise = { exerciseId, reps, weight ->
            viewModel.addSetToExercise(exerciseId, reps, weight)
        },
        deleteSetFromExercise = { setId -> viewModel.deleteSetFromExercise(setId) },
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
    addSetToExercise: (
        exerciseId: Int,
        reps: Int,
        weight: Double,
    ) -> Unit,
    deleteSetFromExercise: (Int) -> Unit,
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
                val populatedExercises = sessionUiState.session.exercises

                if (populatedExercises.isEmpty()) {
                    EmptySection(text = stringResource(R.string.no_exercises))
                } else {
                    LazyColumn(
                        state = listState,
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(count = populatedExercises.size) {
                            val populatedExercise = populatedExercises[it]
                            ExerciseItem(
                                populatedExercise = populatedExercise,
                                deleteExerciseFromSession = deleteExerciseFromSession,
                                addSetToExercise = addSetToExercise,
                                deleteSetFromExercise = deleteSetFromExercise,
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
    populatedExercise: PopulatedExercise,
    deleteExerciseFromSession: (Int) -> Unit,
    addSetToExercise: (
        exerciseId: Int,
        reps: Int,
        weight: Double,
    ) -> Unit,
    deleteSetFromExercise: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var showAddSetDialog by remember { mutableStateOf(false) }

    val exercise = populatedExercise.exercise

    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = exercise.name,
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
                    deleteExerciseFromSession = {
                        deleteExerciseFromSession(exercise.id)
                    },
                )
            }
            IconButton(onClick = { showAddSetDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        }

        Column {
            populatedExercise.sets.forEachIndexed { index, set ->
                SetItem(
                    index = "${index + 1}",
                    reps = set.reps,
                    weight = set.weight,
                    deleteSetFromExercise = {
                        deleteSetFromExercise(set.id)
                    },
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                )
            }
        }
    }

    if (showAddSetDialog) {
        val weightTextState = rememberTextFieldState()
        val repsTextState = rememberTextFieldState()
        AddSetDialog(
            weightTextState = weightTextState,
            repsTextState = repsTextState,
            title = stringResource(R.string.add_set),
            description = stringResource(R.string.add_set_description, exercise.name),
            weightTextFieldLabel = stringResource(R.string.weight),
            repsTextFieldLabel = stringResource(R.string.reps),
            cancelButtonTitle = stringResource(R.string.cancel_button_title),
            confirmButtonTitle = stringResource(R.string.create_button_title),
            onDismissRequest = { showAddSetDialog = false },
            onConfirmation = {
                addSetToExercise(
                    exercise.id,
                    repsTextState.text.toString().toInt(),
                    weightTextState.text.toString().toDouble(),
                )
                showAddSetDialog = false
            },
        )
    }
}

@Composable
fun SetItem(
    index: String,
    reps: Int,
    weight: Double,
    deleteSetFromExercise: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showActionSheet by rememberSaveable { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    Row(
        modifier =
            modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    showActionSheet = true
                },
            ),
    ) {
        Text(
            text = "$index.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(
            text = "$reps",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "x",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Text(
            text = "%.1f".format(weight),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(
            text = "kg",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (showActionSheet) {
        ActionSheet(
            onDismissSheet = { showActionSheet = false },
            onDelete = {
                deleteSetFromExercise()
            },
        )
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

@Composable
fun AddSetDialog(
    weightTextState: TextFieldState,
    repsTextState: TextFieldState,
    title: String,
    description: String,
    weightTextFieldLabel: String,
    repsTextFieldLabel: String,
    cancelButtonTitle: String,
    confirmButtonTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weightErrorMessage by rememberNumberErrorMessage(
        textFieldState = weightTextState,
        min = 0.0,
        max = 99999.0,
        errorMessage = stringResource(R.string.weightErrorMessage),
    )

    val repsErrorMessage by rememberNumberErrorMessage(
        textFieldState = repsTextState,
        min = 0.0,
        max = 99999.0,
        errorMessage = stringResource(R.string.repsErrorMessage),
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(top = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 16.dp),
                )

                Row {
                    OutlinedTextField(
                        state = weightTextState,
                        label = { Text(weightTextFieldLabel) },
                        isError = weightErrorMessage.isNotEmpty(),
                        supportingText = {
                            if (weightErrorMessage.isNotEmpty()) {
                                Text(
                                    text = weightErrorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                        modifier =
                            Modifier
                                .padding(start = 8.dp, top = 8.dp)
                                .weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        state = repsTextState,
                        label = { Text(repsTextFieldLabel) },
                        isError = repsErrorMessage.isNotEmpty(),
                        supportingText = {
                            if (repsErrorMessage.isNotEmpty()) {
                                Text(
                                    text = repsErrorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                        modifier =
                            Modifier
                                .padding(top = 8.dp, end = 8.dp)
                                .weight(1f),
                    )
                }
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(cancelButtonTitle)
                    }
                    TextButton(
                        onClick = onConfirmation,
                        enabled =
                            repsTextState.text.isNotEmpty() &&
                                weightTextState.text.isNotEmpty() &&
                                repsErrorMessage.isEmpty() && weightErrorMessage.isEmpty(),
                    ) {
                        Text(confirmButtonTitle)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberNumberErrorMessage(
    textFieldState: TextFieldState,
    min: Double,
    max: Double,
    errorMessage: String,
) = remember {
    derivedStateOf {
        val text = textFieldState.text.toString()
        if (text.isEmpty()) {
            return@derivedStateOf ""
        }

        val value = text.toDoubleOrNull()
        if (value != null && value >= min && value <= max) {
            ""
        } else {
            errorMessage
        }
    }
}
