package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ScoreChart(data: List<Float>) {
    val lineColor = Color(0xFF4CAF50)
    val pointColor = Color.White
    val fillColorStart = Color(0xFF4CAF50).copy(alpha = 0.3f)
    val fillColorEnd = Color(0xFF4CAF50).copy(alpha = 0.0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(35.dp),
            ) {
                listOf(100, 75, 50, 25, 0).forEachIndexed { index, value ->
                    val bias = -1f + (index * 0.5f)
                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.align(BiasAlignment(1f, bias))
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Graph(
                data = data,
                lineColor = lineColor,
                pointColor = pointColor,
                fillColorStart = fillColorStart,
                fillColorEnd = fillColorEnd,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.indices.forEach { index ->
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun Graph(
    data: List<Float>,
    lineColor: Color,
    pointColor: Color,
    fillColorStart: Color,
    fillColorEnd: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val maxDataVal = 100f

        val verticalPadding = 10.dp.toPx()

        val graphHeight = height - (2 * verticalPadding)

        val spacing = width / (data.size - 1).coerceAtLeast(1)

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        for (i in 0..4) {
            val percentage = 25f * i
            val y = (height - verticalPadding) - (percentage / maxDataVal * graphHeight)

            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = if (i in 1..<4) dashEffect else null
            )
        }

        val strokePath = Path().apply {
            val firstY = (height - verticalPadding) - (data[0] / maxDataVal * graphHeight)
            moveTo(0f, firstY)

            for (i in 1..data.lastIndex) {
                val x = spacing * i
                val y = (height - verticalPadding) - (data[i] / maxDataVal * graphHeight)
                lineTo(x, y)
            }
        }

        val fillPath = Path()
        fillPath.addPath(strokePath)
        fillPath.lineTo(width, height - verticalPadding)
        fillPath.lineTo(0f, height - verticalPadding)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColorStart, fillColorEnd),
                startY = verticalPadding,
                endY = height - verticalPadding
            )
        )

        drawPath(
            path = strokePath,
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        for (i in data.indices) {
            val x = spacing * i
            val y = (height - verticalPadding) - (data[i] / maxDataVal * graphHeight)

            drawCircle(
                color = lineColor,
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )

            drawCircle(
                color = pointColor,
                radius = 3.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}