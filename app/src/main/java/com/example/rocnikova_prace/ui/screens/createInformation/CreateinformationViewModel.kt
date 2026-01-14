package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.model.QuestionType
import com.example.rocnikova_prace.data.repository.QuestionRepository
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel
import kotlinx.coroutines.launch

class CreateInformationViewModel(
    private val repository: QuestionRepository,
    val groupId: String
): ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var showErrors by mutableStateOf(false)
        private set

    var questions = mutableStateListOf<QuestionItem>()
        private set

    private val questionsToDelete = mutableListOf<QuestionItem>()

    var groupName by mutableStateOf("")
        private set

    var validationErrorTrigger by mutableStateOf(0L)
        private set

    init {
        viewModelScope.launch {
            val group = repository.getGroupById(groupId)
            if (group != null) {
                groupName = group.name
            }

            repository.getQuestionsForGroup(groupId).collect { dbQuestions ->
                questions.clear()
                questions.addAll(dbQuestions)
                isLoading = false
                return@collect
            }
            isLoading = false
        }
    }

    // --- WORK WITH GROUP ---
    fun groupNameChange(text: String) {
        groupName = text
    }

    // --- WORK WITH QUESTIONS ---
    fun addQuestion() {
        showErrors = false

        val newQuestion = QuestionItem.emptyMultipleChoice(groupId = groupId)
        questions.add(newQuestion)
    }

    fun removeQuestion(id: String) {
        val question = questions.find { it.id == id }
        if (question != null) {
            questions.remove(question)
            questionsToDelete.add(question)
        }
    }

    fun changeQuestionType(id: String, type: QuestionType) {
        val index = questions.indexOfFirst { it.id == id }
        if (index == -1) return

        val oldQuestion = questions[index]

        val newQuestion = when (type) {
            QuestionType.MultipleChoice -> QuestionItem.emptyMultipleChoice(groupId = groupId, id = id, question = oldQuestion.question)
            QuestionType.Open -> QuestionItem.emptyOpen(groupId = groupId, id = id, question = oldQuestion.question)
            QuestionType.FillBlank -> QuestionItem.emptyFillBlank(groupId = groupId, id = id, question = oldQuestion.question)
        }

        questions[index] = newQuestion
    }

    fun updateQuestion(updated: QuestionItem) {
        val index = questions.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            questions[index] = updated
        }
    }

    fun saveInformation(onSuccess: () -> Unit) {
        if (!isValid()) {
            showErrors = true
            validationErrorTrigger = System.currentTimeMillis()
            return
        }

        viewModelScope.launch {
            repository.saveGroup(GroupEntity(id = groupId, name = groupName))

            questionsToDelete.forEach { repository.deleteQuestion(it) }
            questionsToDelete.clear()

            questions.forEach { repository.saveQuestion(it) }

            onSuccess()
        }
    }

    // --- VALIDATION LOGIC ---
    private fun isValid(): Boolean {
        if (groupName.isBlank()) return false

        return questions.all { question ->
            val isQuestionTextValid = question.question.isNotBlank()

            val isAnswerValid = when (question) {
                is QuestionItem.MultipleChoice -> {
                    val hasCorrectSelection = question.correctIndices.contains(true)

                    val areOptionsFilled = question.options.all { it.isNotBlank() }

                    hasCorrectSelection && areOptionsFilled
                }
                is QuestionItem.Open -> {
                    question.answer.isNotBlank()
                }
                is QuestionItem.FillBlank -> {
                    question.answer.isNotBlank()
                }
            }

            isQuestionTextValid && isAnswerValid
        }
    }
}

class QuestionGroupViewModelFactory(
    private val repository: QuestionRepository,
    private val groupId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateInformationViewModel::class.java) -> {
                CreateInformationViewModel(repository, groupId) as T
            }
            modelClass.isAssignableFrom(PracticeScreenViewModel::class.java) -> {
                PracticeScreenViewModel(repository, groupId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}