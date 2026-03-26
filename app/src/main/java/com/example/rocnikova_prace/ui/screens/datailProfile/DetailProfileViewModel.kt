package com.example.rocnikova_prace.ui.screens.datailProfile

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.rocnikova_prace.data.repository.AuthRepository

class DetailProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
): ViewModel() {
    val userId: String = savedStateHandle["userId"]
        ?: throw IllegalArgumentException("UserId is missing in navigation")


}

class DetailProfileViewModelFactory(
    private val repository: AuthRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return DetailProfileViewModel(handle, repository) as T
    }
}