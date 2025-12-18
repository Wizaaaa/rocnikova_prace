package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun QuestionsScreen(
    viewModel: QuestionsScreenViewModel
) {
    when {
        viewModel.isLoading -> {
            Text("isLoading")
        }

        viewModel.questions.isEmpty() -> {
            Text("isEmpty")
        }

        else -> {
            Text(viewModel.questions.toString())
        }
    }
}