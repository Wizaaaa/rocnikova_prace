package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.createInformation.CreateInformationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickCard(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    viewModel: CreateInformationViewModel
) {
    val groupName = viewModel.groupName

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.CCC_heading))
        },
        text = {
            Column {
                TextField(
                    value = groupName,
                    onValueChange = { viewModel.groupNameChange(it) },
                    placeholder = { Text(stringResource(R.string.CCC_place_holder)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(groupName) },
                enabled = groupName.isNotBlank()
            ) {
                Text(stringResource(R.string.CCC_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.CCC_cancel))
            }
        }
    )
}



@Preview
@Composable
fun ClickCardPreview() {
    ClickCard(
        onDismissRequest = {  },
        onConfirm = {  },
        viewModel()
    )
}