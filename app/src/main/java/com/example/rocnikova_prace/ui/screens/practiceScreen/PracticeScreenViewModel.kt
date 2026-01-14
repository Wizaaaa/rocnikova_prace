package com.example.rocnikova_prace.ui.screens.practiceScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PracticeScreenViewModel(
    private val repository: QuestionRepository,
    private val groupId: String
): ViewModel() {
    private var allQuestions: List<QuestionEntity> = emptyList()

    var currentQuestionIndex by mutableIntStateOf(0)
        private set

    var isRunning = mutableStateOf(false)
        private set

    var timeLeft by mutableIntStateOf(900)
        private set

    private var timerJob: Job? = null

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

    private fun loadData() {
        viewModelScope.launch {
            allQuestions = repository.getQuestionsOnce(groupId).shuffled()

            currentQuestionIndex = 0
        }
    }
}
