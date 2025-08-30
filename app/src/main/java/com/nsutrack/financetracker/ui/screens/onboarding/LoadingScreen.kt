package com.nsutrack.financetracker.ui.screens.onboarding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit
import kotlinx.coroutines.delay

sealed class SavingsCategory(val message: String) {
    object A : SavingsCategory("Houston, we have a spending problem! But don't worry, we'll fix it.")
    object B : SavingsCategory("You're on the right track, but the scenic route is expensive.")
    object C : SavingsCategory("You're a saving superstar! Are you a wizard?")
    object D : SavingsCategory("Your savings are off the charts! Now go touch some grass.")
}

@Composable
fun LoadingScreen(category: SavingsCategory, onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(5000) // 5-second delay
        onTimeout()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.message,
                color = Yellow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = outfit,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedLoadingIndicator()
            }
        }
    }
}

@Composable
fun AnimatedLoadingIndicator() {
    val transition = rememberInfiniteTransition()
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.size(100.dp)) {
        drawCircle(
            color = Yellow.copy(alpha = 0.5f),
            radius = (size.minDimension / 2) * pulse,
        )
    }
}