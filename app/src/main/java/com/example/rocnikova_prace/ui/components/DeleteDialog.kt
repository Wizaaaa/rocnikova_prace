package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ShieldExclamation

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Heroicons.Outline.ShieldExclamation,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.DD_text),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() },
            ) {
                Text(stringResource(R.string.DD_delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
            ) {
                Text(stringResource(R.string.DD_cancel))
            }
        }
    )
}

@Preview
@Composable
fun PreviewDeleteDialog() {
    DeleteDialog({}, {})
}