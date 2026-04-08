package com.example.rocnikova_prace.ui.screens.profileScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.model.GroupSummary
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.data.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileScreenViewModel(
    private val repository: QuestionRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    private val _overviewData = MutableStateFlow<List<GroupSummary>>(emptyList())
    val overviewData = _overviewData.asStateFlow()

    var currentUserId by mutableStateOf<String?>(null)
        private set

    var userName by mutableStateOf("")
        private set

    var userAvatar: String? by mutableStateOf(null)
        private set

    var mail by mutableStateOf("")
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    private var overviewJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        overviewJob?.cancel()
        overviewJob = viewModelScope.launch {
            isLoading = true
            try {
                val id = authRepository.getCurrentUserId()
                currentUserId = id
                if (id == null) {
                    _overviewData.value = emptyList()
                    isLoading = false
                    isRefreshing = false
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    loadUser(id)
                }

                repository.getGroupsOverviewStream(id)
                    .collect { data ->
                        _overviewData.value = data
                        isLoading = false
                        isRefreshing = false
                    }
            } catch (e: Exception) {
                Log.e("ProfileData", "Chyba: ${e.message}")
                e.printStackTrace()
                isLoading = false
                isRefreshing = false
            }
        }
    }

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    authRepository.signOut()
                }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("SignOut", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
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

    private suspend fun loadUser(id: String) {
        val profile = authRepository.getRemoteProfile(id)
        userName = profile.userName.trim()
        userAvatar = profile.avatarUrl?.takeIf { it.isNotBlank() }
        mail = profile.email
    }

    fun forceRefresh() {
        _overviewData.value = emptyList()
        userName = ""
        userAvatar = null
        mail = ""
        isLoading = true

        loadData()
    }
}
