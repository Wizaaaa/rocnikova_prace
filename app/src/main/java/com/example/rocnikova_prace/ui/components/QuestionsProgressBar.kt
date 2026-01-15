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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuestionsProgressBar(
    currentQuestion: Int,
    maxValue: Int
) {
    val progress = if (maxValue > 0) currentQuestion.toFloat() / maxValue.toFloat() else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(50))
    ) {
        drawRoundRect(
            size = size,
            color = Color.LightGray
        )

        drawRoundRect(
            size = Size(
                height = size.height,
                width = size.width * animatedProgress
            ),
            color = Color.Green
        )
    }
}