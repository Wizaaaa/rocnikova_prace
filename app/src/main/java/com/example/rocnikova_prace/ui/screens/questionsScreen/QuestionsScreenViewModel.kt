package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuestionsScreenViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    var questions = mutableStateListOf<QuestionItem>()
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val result = repository.loadAll()
            questions.clear()
            questions.addAll(result)
            isLoading = false
        }
    }
}
