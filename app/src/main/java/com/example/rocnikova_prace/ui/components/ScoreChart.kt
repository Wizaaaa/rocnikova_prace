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
import com.example.rocnikova_prace.ui.theme.Dimens

@Composable
fun ScoreChart(data: List<Float>) {
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.surface
    val fillColorStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    val fillColorEnd = MaterialTheme.colorScheme.primary.copy(alpha = 0.0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(Dimens.medium)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                .padding(start = 45.dp, top = Dimens.small),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val maxLabels = 10

            val step = maxOf(1, data.size / maxLabels)

            data.indices.forEach { index ->
                val isLabelVisible = (index % step == 0) || (index == data.lastIndex)

                val finalVisibility = isLabelVisible && !(index == data.lastIndex - 1 && step > 1)

                Text(
                    text = if (finalVisibility) "${index + 1}" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
    val gridLineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

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
                color = gridLineColor,
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
                width = if (data.size > 30) 2.dp.toPx() else 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val shouldDrawAllPoints = data.size <= 30

        for (i in data.indices) {
            val x = spacing * i
            val y = (height - verticalPadding) - (data[i] / maxDataVal * graphHeight)

            if (shouldDrawAllPoints || i == data.lastIndex) {
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
}