package com.example.rocnikova_prace.ui.screens.createInformation

import android.util.Log
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
        questions.add(QuestionItem.emptyMultipleChoiceMultiple())
    }

    fun changeQuestionType(id: String, type: QuestionType) {
        val index = questions.indexOfFirst { it.id == id }

        if (index == -1) return

        val newQuestion = when(type) {
            QuestionType.MultipleChoiceMultiple -> QuestionItem.emptyMultipleChoiceMultiple(id)
            QuestionType.Open -> QuestionItem.emptyOpen(id)
            QuestionType.FillBlank -> QuestionItem.emptyFillBlank(id)
        }
        Log.d("fix",questions[index].toString())
        questions[index] = newQuestion
        Log.d("fix",questions[index].toString())

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