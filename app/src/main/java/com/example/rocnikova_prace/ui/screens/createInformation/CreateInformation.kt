package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronDown
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronUp
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.FolderPlus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInformation(
    viewModel: CreateInformationViewModel,
    createScreenViewModel: CreateScreenViewModel,
    modifier: Modifier = Modifier
) {
    val items: List<DropdownItem> = listOf(
        DropdownItem(
            label = "MultipleChoiceSingle",
            questionType = QuestionType.MultipleChoiceSingle
        ),
        DropdownItem(
            label = "MultipleChoiceMultiple",
            questionType = QuestionType.MultipleChoiceMultiple
        ),
        DropdownItem(
            label = "Open",
            questionType = QuestionType.Open
        ),
        DropdownItem(
            label = "FillBlank",
            questionType = QuestionType.FillBlank
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
                    val isContextMenuVisible = questionItem.isExpanded

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable{
                                    viewModel.updateQuestion(
                                        updated = questionItem.changeExpanded(!questionItem.isExpanded),
                                        id = questionItem.id
                                    )
                                }
                                .padding(10.dp)
                            ) {
                                Text(
                                    text = "Vyberte druh otázky",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Icon(
                                    imageVector = if (isContextMenuVisible) {
                                        Heroicons.Outline.ChevronDown
                                    } else {
                                        Heroicons.Outline.ChevronUp
                                    },
                                    contentDescription = null
                                )
                            }

                            if (isContextMenuVisible) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    items.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .clickable {
                                                    viewModel.changeQuestionType(
                                                        id = questionItem.id,
                                                        type = item.questionType
                                                    )
                                                    viewModel.updateQuestion(
                                                        updated = questionItem.changeExpanded(false),
                                                        id = questionItem.id
                                                    )
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Text(item.label)
                                        }
                                    }
                                }
                            }
                        }

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