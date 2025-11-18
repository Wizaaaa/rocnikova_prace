package com.example.rocnikova_prace.ui.createInformation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateInformationViewModel: ViewModel() {
    var groupName by mutableStateOf("")
        private set

    fun groupNameChange(text: String) {
        groupName = text
    }

    fun groupNameReset() {
        groupName = ""
    }
}