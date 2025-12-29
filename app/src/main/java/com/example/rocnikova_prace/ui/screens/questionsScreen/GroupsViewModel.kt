package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    var groups = mutableStateListOf<GroupEntity>()
        private set

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            repository.getAllGroups().collectLatest { dbGroups ->
                groups.clear()
                groups.addAll(dbGroups)

                isLoading = false
            }
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