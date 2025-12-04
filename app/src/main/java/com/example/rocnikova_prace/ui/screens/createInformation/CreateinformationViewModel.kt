package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.model.QuestionType

class CreateInformationViewModel: ViewModel() {
    var questions = mutableStateListOf<QuestionItem>()
        private set

    fun addQuestion() {
        questions.add(QuestionItem.emptyMultipleChoiceSingle())
    }

    fun changeQuestionType(id: String, type: QuestionType) {
        val index = questions.indexOfFirst { it.id == id }

        if (index == -1) return

        val newQuestion = when(type) {
            QuestionType.MultipleChoiceSingle -> QuestionItem.emptyMultipleChoiceSingle(id)
            QuestionType.MultipleChoiceMultiple -> QuestionItem.emptyMultipleChoiceMultiple(id)
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
    }

    var groupName by mutableStateOf("")
        private set

    fun groupNameChange(text: String) {
        groupName = text
    }

    fun groupNameReset() {
        groupName = ""
    }
}