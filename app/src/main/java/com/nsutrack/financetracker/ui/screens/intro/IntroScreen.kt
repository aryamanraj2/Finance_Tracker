package com.nsutrack.financetracker.ui.screens.intro

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(onAnimationFinished: () -> Unit) {
    var visibleText by remember { mutableStateOf("") }
    val fullText = "THE FINANCE TRACKER"

    val infiniteTransition = rememberInfiniteTransition(label = "blinker")
    val blinkerAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blinker"
    )

    LaunchedEffect(Unit) {
        fullText.forEachIndexed { index, _ ->
            visibleText = fullText.substring(0, index + 1)
            delay(75) // Faster, more fluid animation
        }
        delay(1000) // Wait a bit after animation finishes
        onAnimationFinished()
    }

    var chartProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        ) { value, _ ->
            chartProgress = value
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StockChartBackground(progress = chartProgress)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = visibleText,
                    color = Yellow,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit
                )
                Text(
                    text = "|",
                    color = Yellow,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit,
                    modifier = Modifier.alpha(blinkerAlpha)
                )
            }
        }
    }
}

@Composable
fun StockChartBackground(progress: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.8f)
            cubicTo(
                size.width * 0.1f, size.height * 0.7f,
                size.width * 0.2f, size.height * 0.9f,
                size.width * 0.3f, size.height * 0.8f
            )
            cubicTo(
                size.width * 0.4f, size.height * 0.7f,
                size.width * 0.5f, size.height * 0.5f,
                size.width * 0.6f, size.height * 0.6f
            )
            cubicTo(
                size.width * 0.7f, size.height * 0.7f,
                size.width * 0.8f, size.height * 0.4f,
                size.width * 0.9f, size.height * 0.5f
            )
            lineTo(size.width, size.height * 0.4f)
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)

        val segmentPath = Path()
        pathMeasure.getSegment(0f, pathMeasure.length * progress, segmentPath, true)

        drawPath(
            path = segmentPath,
            color = Yellow.copy(alpha = 0.2f),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    IntroScreen {}
}