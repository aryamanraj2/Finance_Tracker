package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

@Composable
fun BarChart(
    data: Map<DayOfWeek, Double>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.values.maxOrNull() ?: 0.0

    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        val spaceBetweenBars = barWidth

        data.entries.forEachIndexed { index, entry ->
            val barHeight = (entry.value / maxValue * size.height).toFloat()
            val x = index * (barWidth + spaceBetweenBars) + spaceBetweenBars / 2
            val y = size.height - barHeight

            drawRect(
                color = Color.Blue,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }
}