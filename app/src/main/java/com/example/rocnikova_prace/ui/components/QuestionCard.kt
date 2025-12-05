package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel

@Composable
fun QuestionCard(
    question: QuestionItem.MultipleChoiceMultiple,
    viewModel: CreateInformationViewModel,
    questionIndex: Int,
    modifier: Modifier = Modifier
) {
    var text by remember(question.id, questionIndex) {
        mutableStateOf<String>(question.options[questionIndex])
    }

    val correctIndices = question.correctIndices.toMutableList()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 10.dp)
    ) {
        InformationCard(
            value = text,
            onValueChange = {
                text = it

                val newOptions = question.options.toMutableList()
                newOptions[questionIndex] = it
                viewModel.updateQuestion(
                    updated = question.copy(options = newOptions),
                    id = question.id
                )
            },
            label = "Zadejte otázku",
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
        )

        Checkbox(
            checked = (question.correctIndices[questionIndex]),
            onCheckedChange = {
                correctIndices[questionIndex] = !correctIndices[questionIndex]
                viewModel.updateQuestion(
                    updated = question.copy(correctIndices = correctIndices),
                    id = question.id
                )
            }
        )
    }
}