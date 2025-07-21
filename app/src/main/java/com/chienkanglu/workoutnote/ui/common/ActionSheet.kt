package com.chienkanglu.workoutnote.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chienkanglu.workoutnote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionSheet(
    onDismissSheet: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissSheet,
    ) {
        ListItem(
            headlineContent = { Text(stringResource(R.string.delete)) },
            leadingContent = { Icon(Icons.Default.Delete, null) },
            modifier =
                modifier.clickable {
                    onDelete()
                    onDismissSheet()
                },
        )
    }
}
