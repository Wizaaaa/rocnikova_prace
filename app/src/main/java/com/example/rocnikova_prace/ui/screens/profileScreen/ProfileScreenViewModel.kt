package com.example.rocnikova_prace.ui.screens.profileScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.data.repository.QuestionRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileScreenViewModel(
    repository: QuestionRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    val overviewData = repository.getGroupsOverviewStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userId: String? = authRepository.getCurrentUserId()

    private val supabase = com.example.rocnikova_prace.data.remote.SupabaseClient.client

    val userEmail: String
        get() = supabase.auth.currentUserOrNull()?.email ?: "Neznámý e-mail"

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

    fun getUserName(): String {
        val user = supabase.auth.currentUserOrNull()
        val name = user?.userMetadata?.get("name")?.toString() ?: "Uživatel"
        return name.replace("\"", "")
    }

    fun getUserAvatarUrl(): String? {
        val user = supabase.auth.currentUserOrNull()
        return user?.userMetadata?.get("avatar_url")?.toString()?.replace("\"", "")
    }
}
