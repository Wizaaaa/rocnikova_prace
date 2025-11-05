package com.example.rocnikova_prace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rocnikova_prace.ui.theme.Rocnikova_praceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rocnikova_praceTheme {
                MainScreen()
            }
        }
    }
}
