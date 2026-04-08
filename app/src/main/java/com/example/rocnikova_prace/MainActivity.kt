package com.example.rocnikova_prace

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.ui.theme.AppTheme
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authRepository = AuthRepository(
            supabase = SupabaseClient.client,
            database = AppDatabase.getDatabase(this)
        )

        enableEdgeToEdge()
        SupabaseClient.client.handleDeeplinks(intent)

        updateTokenAtStartup()

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }

    private fun updateTokenAtStartup() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                val userId = authRepository.getCurrentUserId()

                if (userId != null && token != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            authRepository.updateFcmToken(userId, token)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        SupabaseClient.client.handleDeeplinks(intent)
    }
}