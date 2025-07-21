package com.chienkanglu.workoutnote.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TextFieldDialog(
    state: TextFieldState,
    title: String,
    description: String,
    textFieldLabel: String,
    cancelButtonTitle: String,
    confirmButtonTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                OutlinedTextField(
                    state = state,
                    label = { Text(textFieldLabel) },
                    modifier = Modifier.padding(top = 8.dp),
                )
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
                        enabled = state.text.isNotEmpty(),
                    ) {
                        Text(confirmButtonTitle)
                    }
                }
            }
        }
    }
}
