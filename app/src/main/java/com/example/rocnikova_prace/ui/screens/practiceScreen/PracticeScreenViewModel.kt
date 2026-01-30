package com.example.rocnikova_prace.ui.screens.practiceScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.local.toQuestionItem
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PracticeScreenViewModel(
    private val repository: QuestionRepository,
    private val groupId: String
): ViewModel() {
    var allQuestions by mutableStateOf<List<QuestionEntity>>(emptyList())
        private set

    var currentQuestionIndex by mutableIntStateOf(0)
        private set

    var correctAnswerIndex = mutableStateListOf<Boolean>(false, false, false, false)
        private set

    var practiceEnd = mutableStateOf(false)
        private set

    var groupName by mutableStateOf("")
        private set

    var answers = mutableStateListOf<Boolean>()
        private set

    var isRunning = mutableStateOf(false)
        private set

    var timeLeft by mutableIntStateOf(900)
        private set

    private var timerJob: Job? = null

    init {
        loadData()
    }

    fun setCorrectAnswerIndex(answer: Boolean, index: Int) {
        correctAnswerIndex[index] = answer
    }

    fun startTimer() {
        if (isRunning.value) return

        isRunning.value = true

        timerJob = viewModelScope.launch {
            while (isRunning.value && timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning.value = false
        }
    }

    fun isAnswerValid() {
        when (val currentQuestion = allQuestions[currentQuestionIndex].toQuestionItem()) {
            is QuestionItem.MultipleChoice -> {
                if (currentQuestion.correctIndices == correctAnswerIndex) {
                    submitAnswer(true)
                } else {
                    submitAnswer(false)
                }
            }
            is QuestionItem.Open -> {

            }
            is QuestionItem.FillBlank -> {

            }
        }
    }

    fun addAnswer(answer: Boolean) {
        answers.add(answer)
    }

    fun setPracticeEnd(state: Boolean) {
        practiceEnd.value = state
    }

    fun resetPracticeScreen() {
        currentQuestionIndex = 0
        answers.clear()
        practiceEnd.value = false
    }

    private fun loadData() {
        viewModelScope.launch {
            val group = repository.getGroupById(groupId)
            if (group != null) {
                groupName = group.name
            }

            allQuestions = repository.getQuestionsOnce(groupId).shuffled()
        }
    }

    private fun submitAnswer(answer: Boolean) {
        addAnswer(answer)
        increaseCurrentQuestionIndex()

        for (i in 0..3) {
            setCorrectAnswerIndex(false, i)
        }
    }

    private fun increaseCurrentQuestionIndex() {
        if (currentQuestionIndex + 1 != allQuestions.size) {
            currentQuestionIndex++
        } else {
            practiceEnd.value = true
        }
    }
}
