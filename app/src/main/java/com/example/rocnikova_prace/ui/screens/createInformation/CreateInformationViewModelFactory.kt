package com.example.rocnikova_prace.ui.screens.createInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rocnikova_prace.data.repository.QuestionRepository

class CreateInformationViewModelFactory(
    private val repository: QuestionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateInformationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateInformationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}