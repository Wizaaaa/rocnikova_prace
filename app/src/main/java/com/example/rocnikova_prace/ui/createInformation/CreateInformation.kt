package com.example.rocnikova_prace.ui.createInformation

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateInformation(
    viewModel: CreateInformationViewModel,
    modifier: Modifier = Modifier
) {
    TextField(
        value = viewModel.groupName,
        onValueChange = { viewModel.groupNameChange(it) }
    )

    // TODO: groupNameReset when someone move somewhere else in navBar
}

@Preview
@Composable
fun CreateInformationPreview() {
    CreateInformation(viewModel())
}