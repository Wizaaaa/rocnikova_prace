package com.example.rocnikova_prace.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.rocnikova_prace.ui.components.ScoreChart


@Composable
fun ProfileScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Profile"
        )

        val data = listOf<Float>(0f, 20f, 30f, 50f, 80f, 70f, 100f, 98f, 90f, 100f)

        ScoreChart(data)
    }
}