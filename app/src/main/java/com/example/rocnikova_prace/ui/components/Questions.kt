package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel


@Composable
fun DrawMultipleChoiceMultiple(
    question: QuestionItem.MultipleChoice,
    viewModel: CreateInformationViewModel
) {
    var questionText by remember(question.id) { mutableStateOf(question.question) }

    val isQuestionError = viewModel.showErrors && question.question.isBlank()

    val isAnyCorrect = question.correctIndices.contains(true)
    val isCheckboxError = viewModel.showErrors && !isAnyCorrect

    InformationCard(
        value = questionText,
        onValueChange = { newText ->
            questionText = newText

            viewModel.updateQuestion(
                updated = question.copy(question = questionText),
            )
        },
        label = stringResource(R.string.enter_response),
        isError = isQuestionError,
        supportingText = {
            if (isQuestionError) {
                Text(
                    text = stringResource(R.string.questions_cannot_be_blank),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .shake(isQuestionError, trigger = viewModel.validationErrorTrigger)
    )

    Column(
        modifier = Modifier
            .padding(bottom = 10.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            for (i in 0..1) {
                QuestionCard(
                    question = question,
                    viewModel = viewModel,
                    questionIndex = i,
                    isCheckboxError = isCheckboxError,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            for (i in 2..3) {
                QuestionCard(
                    question = question,
                    viewModel = viewModel,
                    questionIndex = i,
                    isCheckboxError = isCheckboxError,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DrawOpen(
    question: QuestionItem.Open,
    viewModel: CreateInformationViewModel
) {
    val isQuestionError = viewModel.showErrors && question.question.isBlank()
    val isAnswerError = viewModel.showErrors && question.answer.isBlank()

    OpenFillQuestionCards(
        id = question.id,
        initialQuestion = question.question,
        initialAnswer = question.answer,
        answerLabel = stringResource(R.string.enter_response),
        isQuestionError = isQuestionError,
        isAnswerError = isAnswerError,
        onUpdate = { newQuestion, newAnswer ->
            viewModel.updateQuestion(
                updated = question.copy(question = newQuestion, answer = newAnswer),
            )
        }
    )
}

@Composable
fun DrawFillBlank(
    question: QuestionItem.FillBlank,
    viewModel: CreateInformationViewModel
) {
    val isQuestionError = viewModel.showErrors && question.question.isBlank()
    val isAnswerError = viewModel.showErrors && question.answer.isBlank()

    OpenFillQuestionCards(
        id = question.id,
        initialQuestion = question.question,
        initialAnswer = question.answer,
        answerLabel = stringResource(R.string.enter_missing_word),
        isQuestionError = isQuestionError,
        isAnswerError = isAnswerError,
        onUpdate = { newQuestion, newAnswer ->
            viewModel.updateQuestion(
                updated = question.copy(question = newQuestion, answer = newAnswer),
            )
        }
    )
}

@Composable
private fun OpenFillQuestionCards(
    id: String,
    initialQuestion: String,
    initialAnswer: String,
    answerLabel: String,
    isQuestionError: Boolean,
    isAnswerError: Boolean,
    onUpdate: (question: String, answer: String) -> Unit
) {
    var questionText by remember(id) { mutableStateOf(initialQuestion) }
    var answerText by remember(id) { mutableStateOf(initialAnswer) }

    InformationCard(
        value = questionText,
        onValueChange = { newText ->
            questionText = newText
            onUpdate(newText, answerText)
        },
        label = stringResource(R.string.enter_question),
        isError = isQuestionError,
        supportingText = {
            if (isQuestionError) {
                Text(
                    text = stringResource(R.string.questions_cannot_be_blank),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    )

    InformationCard(
        value = answerText,
        onValueChange = { newText ->
            answerText = newText
            onUpdate(questionText, newText)
        },
        isError = isAnswerError,
        supportingText = {
            if (isAnswerError) {
                Text(
                    text = stringResource(R.string.response_cannot_be_blank),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        label = answerLabel,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    )
}