package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDialog(
    imageVector: ImageVector,
    text: String,
    dismissText: String,
    confirmText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    centerButtons: Boolean = false
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
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (centerButtons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(dismissText)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onConfirmation) {
                        Text(confirmText)
                    }
                }
            } else {
                TextButton(onClick = onConfirmation) {
                    Text(confirmText)
                }
            }
        },
        dismissButton = if (centerButtons) null else {
            {
                TextButton(onClick = onDismissRequest) {
                    Text(dismissText)
                }
            }
        }
    )
}