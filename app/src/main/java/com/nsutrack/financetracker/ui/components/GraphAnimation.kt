package com.nsutrack.financetracker.ui.components

import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.nsutrack.financetracker.ui.theme.Yellow
import kotlin.math.abs

private data class Candle(
    val high: Float,
    val low: Float,
    val open: Float,
    val close: Float
)

private val candleData = listOf(
    Candle(high = 110f, low = 90f, open = 100f, close = 105f),
    Candle(high = 115f, low = 100f, open = 105f, close = 110f),
    Candle(high = 120f, low = 105f, open = 110f, close = 112f),
    Candle(high = 118f, low = 95f, open = 112f, close = 100f),
    Candle(high = 110f, low = 85f, open = 100f, close = 90f),
    Candle(high = 105f, low = 88f, open = 90f, close = 102f),
    Candle(high = 112f, low = 100f, open = 102f, close = 108f)
)

@Composable
fun GraphAnimation(modifier: Modifier = Modifier) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = EaseOutExpo)
        ) { value, _ ->
            progress = value
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val candleWidth = size.width / (candleData.size * 2)
        val spacing = candleWidth
        val maxVal = candleData.maxOf { it.high }
        val minVal = candleData.minOf { it.low }
        val range = maxVal - minVal

        val chartHeight = size.height * 0.8f
        val chartBaseY = size.height * 0.9f

        val stopLossY = chartBaseY - chartHeight * 0.25f
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        drawLine(
            color = Yellow.copy(alpha = 0.1f),
            start = Offset(0f, stopLossY),
            end = Offset(size.width, stopLossY),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )

        candleData.forEachIndexed { index, candle ->
            val x = (index * (candleWidth + spacing)) + spacing
            val finalHighY = chartBaseY - chartHeight * ((candle.high - minVal) / range)
            val finalLowY = chartBaseY - chartHeight * ((candle.low - minVal) / range)
            val finalOpenY = chartBaseY - chartHeight * ((candle.open - minVal) / range)
            val finalCloseY = chartBaseY - chartHeight * ((candle.close - minVal) / range)

            val animatedHighY = chartBaseY - (chartBaseY - finalHighY) * progress
            val animatedLowY = chartBaseY - (chartBaseY - finalLowY) * progress
            val animatedOpenY = chartBaseY - (chartBaseY - finalOpenY) * progress
            val animatedCloseY = chartBaseY - (chartBaseY - finalCloseY) * progress

            val isBullish = candle.close >= candle.open

            // Draw wick
            drawLine(
                color = Yellow.copy(alpha = 0.2f),
                start = Offset(x + candleWidth / 2, animatedHighY),
                end = Offset(x + candleWidth / 2, animatedLowY),
                strokeWidth = 2f
            )

            // Draw body
            if (isBullish) {
                drawRect(
                    color = Yellow.copy(alpha = 0.05f),
                    topLeft = Offset(x, animatedCloseY),
                    size = androidx.compose.ui.geometry.Size(candleWidth, abs(animatedOpenY - animatedCloseY)),
                    style = Stroke(width = 2f)
                )
            } else {
                drawRect(
                    color = Yellow.copy(alpha = 0.2f),
                    topLeft = Offset(x, animatedOpenY),
                    size = androidx.compose.ui.geometry.Size(candleWidth, abs(animatedOpenY - animatedCloseY))
                )
            }
        }
    }
}