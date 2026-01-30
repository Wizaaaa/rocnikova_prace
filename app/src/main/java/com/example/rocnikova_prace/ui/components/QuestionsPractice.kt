package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
private fun AnswerCard(
    questions: List<Pair<Int, String>>,
    index: IntRange,
    viewModel: PracticeScreenViewModel,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in index) {
            val (originalIndex, text) = questions[i]

            val isChecked = viewModel.correctAnswerIndex[originalIndex]

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.LightGray)
                    .padding(5.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
                )

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { viewModel.setCorrectAnswerIndex(!isChecked, originalIndex) }
                )
            }
        }
    }
}