package com.example.rocnikova_prace.services

import android.util.Log
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Zpráva přijata (popředí): ${remoteMessage.notification?.title}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nový token vygenerován: $token")

        val repository = AuthRepository(
            supabase = SupabaseClient.client,
            database = AppDatabase.getDatabase(applicationContext)
        )

        serviceScope.launch {
            try {
                val userId = repository.getCurrentUserId()
                if (userId != null) {
                    repository.updateFcmToken(userId, token)
                    Log.d("FCM", "Token z onNewToken úspěšně uložen do DB.")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Chyba při ukládání tokenu z onNewToken: ${e.message}")
            }
        }
    }
}