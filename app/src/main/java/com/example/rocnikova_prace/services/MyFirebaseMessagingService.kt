package com.example.rocnikova_prace.services

import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val repository = AuthRepository(
            supabase = SupabaseClient.client,
            database = AppDatabase.getDatabase(applicationContext)
        )

        serviceScope.launch {
            try {
                val userId = repository.getCurrentUserId()
                if (userId != null) {
                    repository.updateFcmToken(userId, token)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}