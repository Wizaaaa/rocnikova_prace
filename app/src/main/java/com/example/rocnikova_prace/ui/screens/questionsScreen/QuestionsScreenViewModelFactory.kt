package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rocnikova_prace.data.repository.QuestionRepository

class QuestionsScreenViewModelFactory(
    private val repository: QuestionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionsScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}