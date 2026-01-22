package com.example.rocnikova_prace.ui.screens.practiceScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.data.local.toQuestionItem
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.components.PracticeMultipleChoice
import com.example.rocnikova_prace.ui.components.QuestionsProgressBar
import java.util.Locale

@Composable
fun PracticeScreen(
    viewModel: PracticeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val minutes = viewModel.timeLeft / 60
    val seconds = viewModel.timeLeft % 60


    if (!viewModel.allQuestions.isEmpty()) {
        val currentQuestion = viewModel.allQuestions[viewModel.currentQuestionIndex].toQuestionItem()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            LaunchedEffect(Unit) {
                viewModel.startTimer()
            }

            Text(
                text = String.format(Locale.GERMANY, "%02d : %02d", minutes, seconds),
                style = MaterialTheme.typography.headlineSmall
            )

            QuestionsProgressBar(viewModel.allQuestions.size, viewModel)



            when (currentQuestion) {
                is QuestionItem.MultipleChoice -> {
                    PracticeMultipleChoice(
                        currentQuestion,
                        viewModel = viewModel
                    )
                }
                is QuestionItem.Open -> {

                }
                is QuestionItem.FillBlank -> {

                }
            }


            TextButton(
                onClick = {
                    viewModel.isAnswerValid()
                }
            ) {
                Text("Další otázka")
            }
        }
    }
}