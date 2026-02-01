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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ScoreChart(data: List<Float>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
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
                Text(
                    text = "100",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(BiasAlignment(1f, -1f))
                )
                Text(
                    text = "75",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(BiasAlignment(1f, -0.5f))
                )
                Text(
                    text = "50",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(BiasAlignment(1f, 0f))
                )
                Text(
                    text = "25",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(BiasAlignment(1f, 0.5f))
                )
                Text(
                    text = "0",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(BiasAlignment(1f, 1f))
                )
            }

            Spacer(Modifier.width(10.dp))


            Graph(
                data = data,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.indices.forEach { index ->
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun Graph(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        if (data.isEmpty()) return@Canvas

        drawRect(
            color = Color.Green.copy(alpha = 0.1f)
        )

        val width = size.width
        val height = size.height

        val maxDataVal = 100f

        val spacing = width / (data.size - 1).coerceAtLeast(1)

        val path = Path().apply {
            val firstY = height - (data[0] / maxDataVal * height)
            moveTo(0f, firstY)

            for (i in 1 .. data.lastIndex) {
                val x = spacing * i
                val y = height - (data[i] / maxDataVal * height)
                lineTo(x, y)
            }
        }

        drawLine(
            color = Color.LightGray,
            start = Offset(0f, height - (50f / maxDataVal * height)),
            end = Offset(width, height - (50f / maxDataVal * height))
        )

        drawPath(
            path = path,
            color = Color.LightGray,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}