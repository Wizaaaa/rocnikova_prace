package com.example.rocnikova_prace.data.model

import com.example.rocnikova_prace.data.local.entities.GroupEntity

data class GroupsUiState(
    val groups: List<GroupEntity> = emptyList(),
    val isLoading: Boolean = true
)
