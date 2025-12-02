package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.rocnikova_prace.data.model.DropdownItem
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.model.QuestionType
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.DrawFillBlank
import com.example.rocnikova_prace.ui.components.DrawMultipleChoiceMultiple
import com.example.rocnikova_prace.ui.components.DrawMultipleChoiceSingle
import com.example.rocnikova_prace.ui.components.DrawOpen
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

    val items: List<DropdownItem> = listOf(
        DropdownItem(
            label = "MultipleChoiceSingle",
            onClick = {
                viewModel.addQuestion(QuestionType.MultipleChoiceSingle)
                isContextMenuVisible = false
            }
        ),
        DropdownItem(
            label = "MultipleChoiceMultiple",
            onClick = {
                viewModel.addQuestion(QuestionType.MultipleChoiceMultiple)
                isContextMenuVisible = false
            }
        ),
        DropdownItem(
            label = "Open",
            onClick = {
                viewModel.addQuestion(QuestionType.Open)
                isContextMenuVisible = false
            }
        ),
        DropdownItem(
            label = "FillBlank",
            onClick = {
                viewModel.addQuestion(QuestionType.FillBlank)
                isContextMenuVisible = false
            }
        )
    )


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
                label = "Zadejte název skupiny otázek",
                modifier = Modifier.fillMaxWidth()
            )


            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                itemsIndexed(
                    items = viewModel.questions,
                    key = { _, item -> item.id }
                ) { index, questionItem ->
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


                    when (questionItem) {
                        is QuestionItem.MultipleChoiceSingle -> {
                            DrawMultipleChoiceSingle(
                                question = questionItem,
                                viewModel = viewModel
                            )
                        }
                        is QuestionItem.MultipleChoiceMultiple -> {
                            DrawMultipleChoiceMultiple()
                        }
                        is QuestionItem.Open -> {
                            DrawOpen()
                        }
                        is QuestionItem.FillBlank -> {
                            DrawFillBlank()
                        }
                    }
                    }
                }
            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false }
            ) {
                items.forEach {
                    DropdownMenuItem(
                        text = { Text(it.label) },
                        onClick = it.onClick
                    )
                }
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