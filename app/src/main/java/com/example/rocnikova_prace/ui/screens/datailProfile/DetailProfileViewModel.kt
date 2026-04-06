package com.example.rocnikova_prace.ui.screens.datailProfile

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.example.rocnikova_prace.data.model.GroupSummary
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val questionRepository: QuestionRepository
): ViewModel() {
    val userId: String = savedStateHandle["userId"]
        ?: throw IllegalArgumentException("UserId is missing in navigation")

    var userName by mutableStateOf("")
        private set

    var mail by mutableStateOf("")
        private set

    var userAvatar: String? by mutableStateOf(null)
        private set

    var notificationsEnabled by mutableStateOf(true)
        private set

    private val _overviewData = MutableStateFlow<List<GroupSummary>>(emptyList())
    val overviewData = _overviewData.asStateFlow()

    var totalProgress by mutableFloatStateOf(0f)
        private set

    init {
        viewModelScope.launch {
            loadUser()
            loadOverviewData()
        }
    }

    suspend fun loadUser() {
        val profile = authRepository.getRemoteProfile(userId)
        userName = profile.userName
        userAvatar = profile.avatarUrl
        mail = profile.email
        notificationsEnabled = profile.notificationsEnabled
    }

    private fun loadOverviewData() {
        viewModelScope.launch {
            questionRepository.getGroupsOverviewStream(userId)
                .collect { data ->
                    _overviewData.value = data
                    calculateTotalProgress(data)
                }
        }
    }

    private fun calculateTotalProgress(overview: List<GroupSummary>) {
        totalProgress = if (overview.isEmpty()) {
            0f
        } else {
            val totalAttempts = overview.sumOf { it.totalAttempts }
            if (totalAttempts == 0) 0f
            else (overview.sumOf { it.averageScore.toDouble() * it.totalAttempts } / totalAttempts).toFloat()
        }
    }

    fun uploadNewAvatar(imageBytes: ByteArray) {
        viewModelScope.launch {
            try {
                authRepository.uploadAvatar(imageBytes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeNotificationsSettings() {
        viewModelScope.launch {
            try {
                val newStatus = authRepository.toggleNotifications(userId)
                notificationsEnabled = newStatus
                Log.d("AuthRepo", "UI aktualizováno na: $newStatus")
            } catch (e: Exception) {
                Log.e("AuthRepo", "Chyba ve ViewModelu: ${e.message}")
            }
        }
    }
}

class DetailProfileViewModelFactory(
    private val authRepository: AuthRepository,
    private val questionRepository: QuestionRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return DetailProfileViewModel(handle, authRepository, questionRepository) as T
    }
}
