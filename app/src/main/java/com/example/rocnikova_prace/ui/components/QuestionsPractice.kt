package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel
import com.example.rocnikova_prace.ui.theme.Dimens

@Composable
fun PracticeMultipleChoice(
    question: QuestionItem.MultipleChoice,
    viewModel: PracticeScreenViewModel
) {
    val shuffledQuestions = remember(question) {
        question.answers.mapIndexed { index, string ->
            index to string
        }.shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge
        )

        AnswerCard(
            questions = shuffledQuestions,
            viewModel = viewModel,
            index = 0..1
        )

        AnswerCard(
            questions = shuffledQuestions,
            viewModel = viewModel,
            index = 2..3
        )

    }
}

@Composable
fun PracticeOpen(
    question: QuestionItem.Open,
    viewModel: PracticeScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge
        )

        InformationCard(
            value = viewModel.questionAnswer,
            onValueChange = { newText ->
                if (viewModel.answerError == "") {
                    viewModel.updateQuestionAnswer(newText)
                }
            },
            isError = viewModel.showError,
            label = stringResource(R.string.enter_response)
        )
    }
}

@Composable
fun PracticeFillBlank(
    question: QuestionItem.FillBlank,
    viewModel: PracticeScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge
        )

        InformationCard(
            value = viewModel.questionAnswer,
            onValueChange = { newText ->
                if (viewModel.answerError != "") {
                    viewModel.updateQuestionAnswer(newText)
                }
            },
            isError = viewModel.showError,
            label = stringResource(R.string.enter_response)
        )
    }
}

@Composable
private fun AnswerCard(
    questions: List<Pair<Int, String>>,
    index: IntRange,
    viewModel: PracticeScreenViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in index) {
            val (originalIndex, text) = questions[i]

            val isChecked = viewModel.correctAnswerIndex[originalIndex]
            val isGreen = viewModel.greenAnswer[originalIndex]
            val isRed = viewModel.redAnswer[originalIndex]

            val isReviewing = viewModel.answerError.isNotEmpty()

            val successContainer = if (isSystemInDarkTheme()) Color(0xFF1B5E20) else Color(0xFFC8E6C9)
            val errorContainer = if (isSystemInDarkTheme()) Color(0xFFB71C1C) else Color(0xFFFFCDD2)

            val backgroundColor = when {
                isRed -> errorContainer
                isGreen -> successContainer
                else -> MaterialTheme.colorScheme.outlineVariant
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(backgroundColor)
                    .clickable(enabled = !isReviewing ){
                        viewModel.setCorrectAnswerIndex(!isChecked, originalIndex)
                    }
                    .padding(5.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = Dimens.small, bottom = Dimens.small, end = Dimens.tiny)
                )

                Checkbox(
                    checked = isChecked,
                    enabled = !isReviewing,
                    onCheckedChange = { viewModel.setCorrectAnswerIndex(!isChecked, originalIndex) }
                )
            }
        }
    }
}