package com.example.rocnikova_prace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.repository.QuestionRepository
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.example.rocnikova_prace.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dao = AppDatabase.getDatabase(this).questionDao()
        val repository = QuestionRepository(dao)

        val viewModel = CreateInformationViewModel(repository)

        setContent {
            AppTheme {
                MainScreen(createInformationViewModel = viewModel)
            }
        }
    }
}
