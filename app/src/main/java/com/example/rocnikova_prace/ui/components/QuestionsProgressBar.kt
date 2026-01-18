package com.example.rocnikova_prace.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel

@Composable
fun QuestionsProgressBar(
    maxValue: Int,
    viewModel: PracticeScreenViewModel
) {
    val animatedProgress by animateFloatAsState(
        targetValue = viewModel.answers.size.toFloat(),
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(50))
    ) {
        val rectWidth = size.width / maxValue


        drawRoundRect(
            size = size,
            color = Color.LightGray
        )

        viewModel.answers.forEachIndexed { index, answer ->
            val segmentProgress = (animatedProgress - index).coerceIn(0f, 1f)

            val xOffset = rectWidth * index
            val color = if (answer) Color.Green else Color.Red


            drawRoundRect(
                size = Size(
                    height = size.height,
                    width = (size.width / maxValue) * segmentProgress
                ),
                topLeft = Offset(x = xOffset, y = Offset.Zero.y),
                color = color
            )
        }
    }
}