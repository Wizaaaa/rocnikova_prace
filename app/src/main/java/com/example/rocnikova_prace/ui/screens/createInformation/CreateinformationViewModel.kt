package com.example.rocnikova_prace.ui.screens.createInformation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.model.QuestionType
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateInformationViewModel(
    private val repository: QuestionRepository
): ViewModel() {
    private var saveJob: Job? = null

    var questions = mutableStateListOf<QuestionItem>()
        private set

    fun addQuestion() {
        questions.add(QuestionItem.emptyMultipleChoice())
    }

    fun removeQuestion(id: String) {
        val index = questions.indexOfFirst { it.id == id }

        questions.removeAt(index)
    }

    fun changeQuestionType(id: String, type: QuestionType) {
        val index = questions.indexOfFirst { it.id == id }

        if (index == -1) return

        val newQuestion = when(type) {
            QuestionType.MultipleChoice -> QuestionItem.emptyMultipleChoice(id)
            QuestionType.Open -> QuestionItem.emptyOpen(id)
            QuestionType.FillBlank -> QuestionItem.emptyFillBlank(id)
        }
        questions[index] = newQuestion
    }

    fun updateQuestion(updated: QuestionItem, id: String) {
        val index = questions.indexOfFirst { it.id == id }
        if (index != -1) {
            questions[index] = updated
        }

        // Debounce to database
        saveJob?.cancel()

        saveJob = viewModelScope.launch {
            delay(400)
            repository.save(updated)
        }
    }

    var groupName by mutableStateOf("")
        private set

    fun groupNameChange(text: String) {
        groupName = text
    }
}