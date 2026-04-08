package com.example.rocnikova_prace.ui.screens.authScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

sealed class AuthState {
    object Idle: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class Error(val message: String): AuthState()
}

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            com.example.rocnikova_prace.data.remote.SupabaseClient.client.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _authState.value = AuthState.Success
                    }
                    is SessionStatus.RefreshFailure -> {
                        if (_authState.value is AuthState.Loading) {
                            _authState.value = AuthState.Error("Odkaz vypršel nebo je neplatný.")
                        }
                    }
                    else -> { }
                }
            }
        }
    }

    fun sendMagicLink() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                com.example.rocnikova_prace.data.remote.SupabaseClient.client.auth.signInWith(OTP, redirectUrl = "rocnikovka://login-callback") {
                    email = this@AuthViewModel.email

                    if (name.isNotBlank()) {
                        data = buildJsonObject {
                            put("name", name)
                            put("full_name", name)
                            put("avatar_url", "")
                        }
                    }
                }
                _authState.value = AuthState.Idle
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Chyba při odesílání: ${e.message}")
            }
        }
    }

    fun saveDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener

            val token = task.result
            val userId = authRepository.getCurrentUserId()

            if (userId != null) {
                viewModelScope.launch {
                    authRepository.updateFcmToken(userId, token)
                }
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updateName(value: String) {
        name = value
    }
}