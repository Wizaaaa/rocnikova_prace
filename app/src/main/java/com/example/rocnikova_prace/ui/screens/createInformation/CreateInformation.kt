package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.data.model.DropdownItem
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.model.QuestionType
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.DrawFillBlank
import com.example.rocnikova_prace.ui.components.DrawMultipleChoice
import com.example.rocnikova_prace.ui.components.DrawOpen
import com.example.rocnikova_prace.ui.components.InformationCard
import com.example.rocnikova_prace.ui.components.shake
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronDown
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronUp
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.FolderPlus
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.XMark

@Composable
fun CreateInformation(
    viewModel: CreateInformationViewModel,
    modifier: Modifier = Modifier
) {
    val items: List<DropdownItem> = listOf(
        DropdownItem(
            label = stringResource(R.string.multipleChoice),
            questionType = QuestionType.MultipleChoice
        ),
        DropdownItem(
            label = stringResource(R.string.open),
            questionType = QuestionType.Open
        ),
        DropdownItem(
            label = stringResource(R.string.fillBlank),
            questionType = QuestionType.FillBlank
        )
    )

    val groupNameError = viewModel.showErrors && viewModel.groupName.isBlank()

    Column(modifier = modifier.fillMaxSize()) {
        InformationCard(
            value = viewModel.groupName,
            onValueChange = { viewModel.groupNameChange(it) },
            label = stringResource(R.string.CI_groupName),
            isError = groupNameError,
            supportingText = {
                if (groupNameError) {
                    Text(
                        text = stringResource(R.string.CI_group_name_cannot_empty),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .shake(groupNameError, trigger = viewModel.validationErrorTrigger)
        )


        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(
                items = viewModel.questions,
                key = { item -> item.id }
            ) { questionItem ->
                val isContextMenuVisible = questionItem.isExpanded

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        viewModel.updateQuestion(
                                            updated = questionItem.changeExpanded(!questionItem.isExpanded),
                                        )
                                    }
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.CI_questionType),
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.weight(1f)
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

                            AnimatedVisibility(
                                visible = isContextMenuVisible,
                                enter = expandVertically(expandFrom = Alignment.Top),
                                exit = shrinkVertically(shrinkTowards = Alignment.Top)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    items.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .clickable {
                                                    viewModel.changeQuestionType(
                                                        id = questionItem.id,
                                                        type = item.questionType,
                                                    )
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Text(item.label)
                                        }
                                    }
                                }
                            }

                            when (questionItem) {
                                is QuestionItem.MultipleChoice -> {
                                    DrawMultipleChoice(
                                        question = questionItem,
                                        viewModel = viewModel
                                    )
                                }
                                is QuestionItem.Open -> {
                                    DrawOpen(
                                        question = questionItem,
                                        viewModel = viewModel
                                    )
                                }
                                is QuestionItem.FillBlank -> {
                                    DrawFillBlank(
                                        question = questionItem,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Heroicons.Outline.XMark,
                            contentDescription = stringResource(R.string.CI_deleteQuestion),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    viewModel.removeQuestion(questionItem.id)
                                }
                        )
                    }
                }
            }
        }

        Cards(
            icon = Heroicons.Outline.FolderPlus,
            text = R.string.new_question,
            onClick = { viewModel.addQuestion() },
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
    }
}

@Preview
@Composable
fun CreateInformationPreview() {
    CreateInformation(viewModel())
}