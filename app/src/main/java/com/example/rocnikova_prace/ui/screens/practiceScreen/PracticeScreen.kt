package com.example.rocnikova_prace.ui.screens.practiceScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.Locale

@Composable
fun PracticeScreen(
    viewModel: PracticeScreenViewModel
) {
    val minutes = viewModel.timeLeft / 60
    val seconds = viewModel.timeLeft % 60

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            viewModel.startTimer()
        }

        Text(
            text = String.format(Locale.GERMANY, "%02d : %02d", minutes, seconds),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}