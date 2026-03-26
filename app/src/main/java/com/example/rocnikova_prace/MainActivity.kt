package com.example.rocnikova_prace

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.ui.theme.AppTheme
import io.github.jan.supabase.auth.handleDeeplinks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        SupabaseClient.client.handleDeeplinks(intent)

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        SupabaseClient.client.handleDeeplinks(intent)
    }
}