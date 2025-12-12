package com.example.rocnikova_prace.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repo: QuestionRepository
) : ViewModel() {

    var questions = mutableStateListOf<QuestionItem>()
        private set

    fun loadQuestions() {
        viewModelScope.launch {
            questions.clear()
            questions.addAll(repo.loadAll())
        }
    }

    fun saveQuestion(item: QuestionItem) {
        viewModelScope.launch {
            repo.save(item)
            if (questions.none { it.id == item.id }) {
                questions.add(item)
            }
        }
    }

    fun deleteQuestion(item: QuestionItem) {
        viewModelScope.launch {
            repo.delete(item)
            questions.removeAll { it.id == item.id }
        }
    }
}