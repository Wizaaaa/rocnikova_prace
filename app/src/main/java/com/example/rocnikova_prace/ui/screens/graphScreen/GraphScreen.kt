package com.example.rocnikova_prace.ui.screens.graphScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.rocnikova_prace.ui.components.ScoreChart

@Composable
fun GraphScreen(
    viewModel: GraphScreenViewModel,
    modifier: Modifier = Modifier
) {
    val data by viewModel.graphData.collectAsState()

    Row(modifier = modifier) {
        ScoreChart(data)
    }
}