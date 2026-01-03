package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.model.GroupsUiState
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val repository: QuestionRepository
) : ViewModel() {
    val uiState: StateFlow<GroupsUiState> = repository.getAllGroups()
        .map { groups ->
            GroupsUiState(groups = groups, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GroupsUiState(isLoading = true)
        )

    fun deleteGroup(group: GroupEntity) {
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }
}

class GroupsViewModelFactory(
    private val repository: QuestionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}