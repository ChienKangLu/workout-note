package com.chienkanglu.workoutnote.exercises

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
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.runtime.remember
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
import com.chienkanglu.workoutnote.data.model.Exercise
import com.chienkanglu.workoutnote.ui.common.ActionSheet
import com.chienkanglu.workoutnote.ui.common.EmptySection
import com.chienkanglu.workoutnote.ui.common.TextFieldDialog

@Composable
internal fun ExercisesScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ExercisesViewModel = hiltViewModel(),
) {
    val exercisesUiState by viewModel.exercisesUiState.collectAsStateWithLifecycle()
    ExercisesScreen(
        exercisesUiState = exercisesUiState,
        addExercise = { viewModel.insertExercise(it) },
        deleteExercises = { ids ->
            viewModel.deleteExercises(ids)
        },
        modifier = modifier,
    )
}

@Composable
fun ExercisesScreen(
    exercisesUiState: ExercisesUiState,
    addExercise: (name: String) -> Unit,
    deleteExercises: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showAddExerciseDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton({
                showAddExerciseDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }

        Text(
            stringResource(R.string.exercises),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = modifier.height(10.dp))

        when (exercisesUiState) {
            is ExercisesUiState.Loading -> Unit
            is ExercisesUiState.Success ->
                if (exercisesUiState.exercises.isEmpty()) {
                    EmptySection(text = stringResource(R.string.no_exercises))
                } else {
                    LazyColumn {
                        itemsIndexed(items = exercisesUiState.exercises) { index, exercise ->
                            ExerciseItem(
                                exercise = exercise,
                                isFirst = index == 0,
                                deleteExercise = deleteExercises,
                                modifier = modifier,
                            )
                        }
                    }
                }
        }

        if (showAddExerciseDialog.value) {
            val exerciseNameSate = rememberTextFieldState()

            TextFieldDialog(
                state = exerciseNameSate,
                modifier = modifier,
                title = stringResource(R.string.dialog_add_exercise_title),
                description = stringResource(R.string.dialog_add_exercise_description),
                textFieldLabel = stringResource(R.string.dialog_add_exercise_text_field_label),
                cancelButtonTitle = stringResource(R.string.cancel_button_title),
                confirmButtonTitle = stringResource(R.string.create_button_title),
                onDismissRequest = {
                    showAddExerciseDialog.value = false
                },
                onConfirmation = {
                    showAddExerciseDialog.value = false
                    addExercise(exerciseNameSate.text.toString())
                },
            )
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    isFirst: Boolean,
    deleteExercise: (List<Int>) -> Unit,
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
                onClick = {},
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    showActionSheet = true
                },
            ).padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (showActionSheet) {
        ActionSheet(
            onDismissSheet = { showActionSheet = false },
            onDelete = {
                deleteExercise(listOf(exercise.id))
            },
        )
    }
}
