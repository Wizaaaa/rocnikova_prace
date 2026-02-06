package com.example.rocnikova_prace.ui.screens.graphScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GraphScreenViewModel(
    repository: QuestionRepository,
    groupId: String
): ViewModel() {
    val graphData: StateFlow<List<Float>> = repository.getTestResultsStream(groupId)
        .map { listOfEntities ->
            listOfEntities.map { it.percentage }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}