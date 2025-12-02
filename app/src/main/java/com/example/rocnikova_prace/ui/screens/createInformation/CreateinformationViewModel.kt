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

    fun addQuestion(type: QuestionType = QuestionType.MultipleChoiceSingle) {
        when(type) {
            QuestionType.MultipleChoiceSingle -> questions.add(QuestionItem.emptyMultipleChoiceSingle())
            QuestionType.MultipleChoiceMultiple -> questions.add(QuestionItem.emptyMultipleChoiceMultiple())
            QuestionType.Open -> questions.add(QuestionItem.emptyOpen())
            QuestionType.FillBlank -> questions.add(QuestionItem.emptyFillBlank())
        }
    }

    fun updateQuestion(updated: QuestionItem, id: String) {
        val index = questions.indexOfFirst { it.id == id }
        if (index != -1) {
            questions[index] = updated
        }
    }

    fun  trueQuestionIsChecked(updated: QuestionItem, id: String) {
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