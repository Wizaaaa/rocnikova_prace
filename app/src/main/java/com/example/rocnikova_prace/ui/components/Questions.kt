package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel


@Composable
fun DrawMultipleChoiceMultiple(
    question: QuestionItem.MultipleChoice,
    viewModel: CreateInformationViewModel
) {
    var questionText by remember(question.id) { mutableStateOf(question.question) }

    InformationCard(
        value = questionText,
        onValueChange = { newText ->
            questionText = newText

            viewModel.updateQuestion(
                updated = question.copy(question = questionText),
                id = question.id
            )
        },
        label = "Zadejte otázku",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    )

    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Row(Modifier.fillMaxWidth()) {
            for (i in 0..1) {
                QuestionCard(
                    question = question,
                    viewModel = viewModel,
                    questionIndex = i,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            for (i in 2..3) {
                QuestionCard(
                    question = question,
                    viewModel = viewModel,
                    questionIndex = i,
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
    OpenFillQuestionCards(
        id = question.id,
        initialQuestion = question.question,
        initialAnswer = question.answer,
        answerLabel = "Zadejte odpověď",
        onUpdate = { newQuestion, newAnswer ->
            viewModel.updateQuestion(
                updated = question.copy(question = newQuestion, answer = newAnswer),
                id = question.id
            )
        }
    )
}

@Composable
fun DrawFillBlank(
    question: QuestionItem.FillBlank,
    viewModel: CreateInformationViewModel
) {
    OpenFillQuestionCards(
        id = question.id,
        initialQuestion = question.question,
        initialAnswer = question.answer,
        answerLabel = "Zadejte vynechané slovo",
        onUpdate = { newQuestion, newAnswer ->
            viewModel.updateQuestion(
                updated = question.copy(question = newQuestion, answer = newAnswer),
                id = question.id
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
    onUpdate: (question: String, answer: String) -> Unit
) {
    var questionText by remember(id) { mutableStateOf(initialQuestion) }
    var answerText by remember(id) { mutableStateOf(initialAnswer) }

    InformationCard(
        value = questionText,
        onValueChange = { newText ->
            questionText = newText
            onUpdate (newText, answerText)
        },
        label = "Zadejte otázku",
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
        label = answerLabel,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    )
}

@Preview
@Composable
fun PreviewQuestions() {
    DrawMultipleChoiceMultiple(
        QuestionItem.emptyMultipleChoice(),
        viewModel = viewModel()
    )
}