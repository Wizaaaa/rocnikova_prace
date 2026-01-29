package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDialog(
    imageVector: ImageVector,
    text: String,
    dismissText: String,
    confirmText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        text = {
            Text(
                text = text,
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
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
            ) {
                Text(dismissText)
            }
        }
    )
}

@Preview
@Composable
fun PreviewDeleteDialog() {
//    DeleteDialog({}, {}, )
}