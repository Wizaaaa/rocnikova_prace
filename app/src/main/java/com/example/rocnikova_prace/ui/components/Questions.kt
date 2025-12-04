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
fun DrawMultipleChoiceSingle(
    question: QuestionItem.MultipleChoiceSingle,
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
            QuestionCard(
                question = question,
                viewModel = viewModel,
                questionIndex = 0,
                modifier = Modifier.weight(1f)
            )

            QuestionCard(
                question = question,
                viewModel = viewModel,
                questionIndex = 1,
                modifier = Modifier.weight(1f)
            )
        }
        Row(Modifier.fillMaxWidth()) {
            QuestionCard(
                question = question,
                viewModel = viewModel,
                questionIndex = 2,
                modifier = Modifier.weight(1f)
            )

            QuestionCard(
                question = question,
                viewModel = viewModel,
                questionIndex = 3,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DrawMultipleChoiceMultiple() {
    Text("DrawMultipleChoiceMultiple")
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
    DrawMultipleChoiceSingle(
        QuestionItem.MultipleChoiceSingle(
            question = "",
            options = listOf("", "", "", ""),
            correctIndex = 0
        ),
        viewModel = viewModel()
    )
}