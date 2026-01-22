package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel

@Composable
fun PracticeMultipleChoice(
    question: QuestionItem.MultipleChoice,
    viewModel: PracticeScreenViewModel
) {
    Text(
        text = question.question
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AnswerCard(
            question = question,
            viewModel = viewModel,
            index = 0..1
        )

        AnswerCard(
            question = question,
            viewModel = viewModel,
            index = 2..3
        )

    }
}

@Composable
private fun AnswerCard(
    question: QuestionItem.MultipleChoice,
    index: IntRange,
    viewModel: PracticeScreenViewModel,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in index) {
            val isChecked = viewModel.correctAnswerIndex[i]

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.LightGray)
            ) {
                Spacer(Modifier.weight(1f))

                Text(
                    text = question.answers[i]
                )

                Spacer(Modifier.weight(1f))

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { viewModel.setCorrectAnswerIndex(!isChecked, i) }
                )
            }
        }
    }
}