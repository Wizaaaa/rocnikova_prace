package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
    question: QuestionItem.MultipleChoiceMultiple,
    viewModel: CreateInformationViewModel
) {
    var questionText by remember(question.id) { mutableStateOf(question.question) }

    InformationCard(
        value = questionText,
        onValueChange = { newText ->
            viewModel.updateQuestion(
                updated = question.copy(question = newText),
                id = question.id
            )
        },
        label = "Zadejte otázku",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    )




    Column{
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
fun DrawOpen() {
    Text("DrawOpen")
}

@Composable
fun DrawFillBlank() {
    Text("DrawFillBlank")
}

@Preview
@Composable
fun PreviewQuestions() {
    DrawMultipleChoiceMultiple(
        QuestionItem.MultipleChoiceMultiple(
            question = "",
            options = listOf("", "", "", ""),
            correctIndices = emptyList()
        ),
        viewModel = viewModel()
    )
}