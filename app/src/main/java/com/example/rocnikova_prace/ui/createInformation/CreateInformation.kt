package com.example.rocnikova_prace.ui.createInformation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.ui.components.InformationCard
import com.example.rocnikova_prace.ui.createScreen.CreateScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInformation(
    viewModel: CreateInformationViewModel,
    createScreenViewModel: CreateScreenViewModel,
    modifier: Modifier = Modifier
) {


    ModalBottomSheet(
        onDismissRequest = {
            createScreenViewModel.onDismissRequest()
            viewModel.groupNameReset()
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
        ) {

            InformationCard(
                value = viewModel.groupName,
                onValueChange = { viewModel.groupNameChange(it) },
                label = "Zadejte název skupiny otázek"
            )
        }

    }
}

@Preview
@Composable
fun CreateInformationPreview() {
    CreateInformation(viewModel(), viewModel())
}