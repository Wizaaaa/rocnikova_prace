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
import kotlin.collections.forEachIndexed

class PracticeScreenViewModel(
    private val repository: QuestionRepository,
    private val groupId: String
): ViewModel() {
    var allQuestions by mutableStateOf<List<QuestionEntity>>(emptyList())
        private set

    var currentQuestionIndex by mutableIntStateOf(0)
        private set

    var correctAnswerIndex = mutableStateListOf(false, false, false, false)
        private set

    var greenAnswer = mutableStateListOf(false, false, false, false)
        private set

    var redAnswer = mutableStateListOf(false, false, false, false)
        private set

    var showError by mutableStateOf(false)
        private set

    var practiceEnd = mutableStateOf(false)
        private set

    var groupName by mutableStateOf("")
        private set

    var questionAnswer by mutableStateOf("")
        private set

    var answers = mutableStateListOf<Boolean>()
        private set

    var isRunning = mutableStateOf(false)
        private set

    val secondsForQuestion = 30

    var timeLeft by mutableIntStateOf(secondsForQuestion)
        private set

    var answerError by mutableStateOf("")
        private set

    private var timerJob: Job? = null

    init {
        loadData()
    }

    fun setCorrectAnswerIndex(answer: Boolean, index: Int) {
        correctAnswerIndex[index] = answer
    }

    fun setGreenAnswer(answer: Boolean, index: Int) {
        greenAnswer[index] = answer
    }

    fun setRedAnswer(answer: Boolean, index: Int) {
        redAnswer[index] = answer
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

    fun pauseTimer() {
        isRunning.value = false

        timerJob?.cancel()
        timerJob = null
    }

    fun isAnswerValid() {
        when (val currentQuestion = allQuestions[currentQuestionIndex].toQuestionItem()) {
            is QuestionItem.MultipleChoice -> {
                if (answerError == "") {
                    val isPerfectMatch = currentQuestion.correctIndices == correctAnswerIndex.toList()

                    if (isPerfectMatch) {
                        correctAnswerIndex.forEachIndexed { index, isCheckedByUser ->
                            if (isCheckedByUser) {
                                setGreenAnswer(true, index)
                            }
                        }
                        addAnswer(true)
                    } else {
                        correctAnswerIndex.forEachIndexed { index, isCheckedByUser ->
                            val isCorrectInDb = currentQuestion.correctIndices[index]

                            if (isCheckedByUser && !isCorrectInDb) {
                                setRedAnswer(true, index)
                            } else if (isCorrectInDb) {
                                setGreenAnswer(true, index)
                                setCorrectAnswerIndex(true, index)
                            }
                        }
                        addAnswer(false)
                    }

                    answerError = " "
                } else {
                    submitOpenAnswer()
                    resetCorrectAnswerIndex()
                    resetColors()
                }
            }
            is QuestionItem.Open -> {
                if (answerError == "") {
                    val enteredAnswer = currentQuestion.answer.lowercase().replace(" ", "")
                    val correctAnswer = questionAnswer.lowercase().replace(" ", "")

                    if (enteredAnswer == correctAnswer) {
                        addAnswer(true)
                        answerError = "Správně"
                    } else {
                        addAnswer(false)
                        showError = true
                        answerError = "Správná odpověd je: ${currentQuestion.answer}"
                    }
                } else {
                    submitOpenAnswer()
                }
            }
            is QuestionItem.FillBlank -> {
                if (answerError == "") {
                    val enteredAnswer = currentQuestion.answer.lowercase().replace(" ", "")
                    val correctAnswer = questionAnswer.lowercase().replace(" ", "")

                    if (enteredAnswer == correctAnswer) {
                        addAnswer(true)
                        answerError = "Správně"
                    } else {
                        addAnswer(false)
                        showError = true
                        answerError = "Správná odpověd je: ${currentQuestion.answer}"
                    }
                } else {
                    submitOpenAnswer()
                }
            }
        }
    }

    fun addAnswer(answer: Boolean) {
        answers.add(answer)
    }

    fun updateQuestionAnswer(answer: String) {
        questionAnswer = answer
    }

    fun setPracticeEnd(state: Boolean) {
        practiceEnd.value = state
    }

    fun resetPracticeScreen() {
        currentQuestionIndex = 0
        answers.clear()
        practiceEnd.value = false
        timeLeft = allQuestions.size * secondsForQuestion
        resetColors()
        resetCorrectAnswerIndex()
        startTimer()
    }

    fun resetColors() {
        for (i in 0..3) {
            setRedAnswer(false, i)
            setGreenAnswer(false, i)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val group = repository.getGroupById(groupId)
            if (group != null) {
                groupName = group.name
            }

            allQuestions = repository.getQuestionsOnce(groupId).shuffled()

            timeLeft = allQuestions.size * secondsForQuestion
        }
    }

    private fun submitOpenAnswer() {
        increaseCurrentQuestionIndex()
        questionAnswer = ""
        answerError = ""
        showError = false
    }

    private fun resetCorrectAnswerIndex() {
        for (i in 0..3) {
            setCorrectAnswerIndex(false, i)
        }
    }

    private fun increaseCurrentQuestionIndex() {
        if (currentQuestionIndex + 1 != allQuestions.size) {
            currentQuestionIndex++
        } else {
            practiceEnd.value = true
            saveResult()
        }
    }

    private fun saveResult() {
        val correctCount = answers.count { it }
        val totalCount = allQuestions.size

        val percentage = if (totalCount > 0) {
            (correctCount.toFloat() / totalCount.toFloat()) * 100f
        } else {
            0f
        }

        viewModelScope.launch {
            repository.saveTestResult(groupId, percentage)
        }
    }
}
