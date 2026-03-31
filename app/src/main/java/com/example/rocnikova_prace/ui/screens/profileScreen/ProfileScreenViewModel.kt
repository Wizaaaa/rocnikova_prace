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

    val userId = authRepository.getCurrentUserId()

    var userName by mutableStateOf("")
        private set

    var userAvatar: String? by mutableStateOf(null)
        private set

    var mail by mutableStateOf("")
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true
            try {
                loadUser()

                repository.getGroupsOverviewStream()
                    .collect { data ->
                        _overviewData.value = data
                        isLoading = false
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
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

    suspend fun loadUser() {
        val id = authRepository.getCurrentUserId()
        val profile = authRepository.getRemoteProfile(id!!)
        userName = profile.userName
        userAvatar = profile.avatarUrl
        mail = profile.email
    }

    fun refreshData() {
        loadData()
    }
}
