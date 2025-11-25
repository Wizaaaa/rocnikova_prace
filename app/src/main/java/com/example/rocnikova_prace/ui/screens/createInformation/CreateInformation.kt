package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.InformationCard
import com.example.rocnikova_prace.ui.screens.createScreen.CreateScreenViewModel
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.FolderPlus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInformation(
    viewModel: CreateInformationViewModel,
    createScreenViewModel: CreateScreenViewModel,
    modifier: Modifier = Modifier
) {
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            createScreenViewModel.onDismissRequest()
            viewModel.groupNameReset()
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            InformationCard(
                value = viewModel.groupName,
                onValueChange = { viewModel.groupNameChange(it) },
                label = "Zadejte název skupiny otázek"
            )


            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                items(viewModel.questions) { questions ->
                    val uiData = questions.toUiData()
                    Card(
                        modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(true) {
                            detectTapGestures(
                                onTap = {
                                    isContextMenuVisible = true
                                }
                            )
                        }
                    ) {
                        InformationCard(
                            value = uiData.question,
                            onValueChange = {  },
                            label = "Zadejte otázku"
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text("idk") },
                    onClick = {  }
                )
            }




            Cards(
                icon = Heroicons.Outline.FolderPlus,
                text = R.string.new_question,
                onClick = { viewModel.addQuestion() }
            )
        }

    }
}

@Preview
@Composable
fun CreateInformationPreview() {
    CreateInformation(viewModel(), viewModel())
}