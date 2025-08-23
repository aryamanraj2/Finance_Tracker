package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedProgressBar(
    segments: List<Pair<Float, Color>>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.height
        var start = 0f
        segments.forEach { (weight, color) ->
            val end = start + weight * size.width
            drawLine(
                color = color,
                start = Offset(start, size.height / 2),
                end = Offset(end, size.height / 2),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            start = end
        }
    }
}

@Preview
@Composable
fun SegmentedProgressBarPreview() {
    val segments = listOf(
        0.4f to Color(0xFFE5DD00),
        0.3f to Color(0xFF82D85B),
        0.1f to Color.White,
        0.2f to Color.Gray
    )
    SegmentedProgressBar(
        segments = segments,
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
    )
}