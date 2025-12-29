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
    import kotlinx.coroutines.Job
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch

    class CreateInformationViewModel(
        private val repository: QuestionRepository,
        val groupId: String
    ): ViewModel() {
        private var saveQuestionJob: Job? = null
        private var saveGroupJob: Job? = null

        var questions = mutableStateListOf<QuestionItem>()
            private set

        var groupName by mutableStateOf("")
            private set

        init {
            viewModelScope.launch {
                val group = repository.getGroupById(groupId)
                if (group != null) {
                    groupName = group.name
                } else {
                    saveGroupToDb()
                }
            }
            viewModelScope.launch {
                repository.getQuestionsForGroup(groupId).collectLatest { dbQuestions ->
                    questions.clear()
                    questions.addAll(dbQuestions)
                }
            }
        }

        // --- PRÁCE SE SKUPINOU ---
        fun groupNameChange(text: String) {
            groupName = text

            saveGroupJob?.cancel()
            saveGroupJob = viewModelScope.launch {
                delay(500)
                saveGroupToDb()
            }
        }

        private suspend fun saveGroupToDb() {
            repository.saveGroup(
                GroupEntity(
                    id = groupId,
                    name = groupName
                )
            )
        }

        // --- PRÁCE S OTÁZKAMI ---
        fun addQuestion() {
            viewModelScope.launch {
                val newQuestion = QuestionItem.emptyMultipleChoice(groupId = groupId)
                repository.saveQuestion(newQuestion)
            }
        }

        fun removeQuestion(id: String) {
            val questionToDelete = questions.find { it.id == id }
            if (questionToDelete != null) {
                viewModelScope.launch {
                    repository.deleteQuestion(questionToDelete)
                }
            }
        }

        fun changeQuestionType(id: String, type: QuestionType) {
            val index = questions.indexOfFirst { it.id == id }
            if (index == -1) return

            val newQuestion = when (type) {
                QuestionType.MultipleChoice -> QuestionItem.emptyMultipleChoice(groupId = groupId, id = id)
                QuestionType.Open -> QuestionItem.emptyOpen(groupId = groupId, id = id)
                QuestionType.FillBlank -> QuestionItem.emptyFillBlank(groupId = groupId, id = id)
            }

            updateQuestionInDb(newQuestion)
        }

        fun updateQuestion(updated: QuestionItem, id: String) {
            val index = questions.indexOfFirst { it.id == id }
            if (index != -1) {
                questions[index] = updated
            }

            saveQuestionJob?.cancel()
            saveQuestionJob = viewModelScope.launch {
                delay(400)
                val finalQuestion = updated.copyWithGroupId(groupId)
                repository.saveQuestion(finalQuestion)
            }
        }

        private fun updateQuestionInDb(question: QuestionItem) {
            viewModelScope.launch {
                repository.saveQuestion(question)
            }
        }
    }

    class CreateInformationViewModelFactory(
        private val repository: QuestionRepository,
        private val groupId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateInformationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateInformationViewModel(repository, groupId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }