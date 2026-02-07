package com.example.rocnikova_prace.ui.screens.graphScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GraphScreenViewModel(
    private val repository: QuestionRepository,
    private val groupId: String
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

    var groupName by mutableStateOf("")
        private set

    init {
        loadGroupName()
    }

    private fun loadGroupName() {
        viewModelScope.launch {
            val group = repository.getGroupById(groupId)
            if (group != null) {
                groupName = group.name
            }
        }
    }
}