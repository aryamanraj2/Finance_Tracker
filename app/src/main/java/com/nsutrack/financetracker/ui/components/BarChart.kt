package com.nsutrack.financetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.utils.formatCurrency
import java.time.DayOfWeek
import kotlin.math.max

@Composable
fun BarChart(
    data: Map<DayOfWeek, Double>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.values.maxOrNull() ?: 0.0
    var startAnimation by remember { mutableStateOf(false) }
    
    // Animation for bars
    val animationProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "barAnimation"
    )
    
    LaunchedEffect(data) {
        startAnimation = true
    }
    
    // Order days from Sunday to Saturday
    val orderedDays = listOf(
        DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    )
    
    val orderedData = orderedDays.map { day ->
        day to (data[day] ?: 0.0)
    }

    Column(modifier = modifier) {
        // Chart area with proper alignment
        Column {
            // Amount labels positioned above bars
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    orderedData.forEach { (_, amount) ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (amount > 0 && animationProgress > 0.8f) {
                                Text(
                                    text = "₹${amount.toInt()}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Chart bars
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                drawBarChart(
                    data = orderedData,
                    maxValue = maxValue,
                    animationProgress = animationProgress
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Day labels properly aligned under bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            orderedData.forEach { (day, _) ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getDayAbbreviation(day),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Max value indicator
        if (maxValue > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Peak: ${formatCurrency(maxValue)}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun DrawScope.drawBarChart(
    data: List<Pair<DayOfWeek, Double>>,
    maxValue: Double,
    animationProgress: Float
) {
    if (data.isEmpty() || maxValue <= 0) return
    
    val barCount = data.size
    val availableWidth = size.width
    val barSpacing = availableWidth * 0.1f / (barCount + 1) // 10% total spacing
    val barWidth = (availableWidth - barSpacing * (barCount + 1)) / barCount
    
    val maxBarHeight = size.height * 0.9f // Use more of the available height
    
    data.forEachIndexed { index, (_, amount) ->
        val targetBarHeight = if (maxValue > 0 && amount > 0) {
            max((amount / maxValue * maxBarHeight).toFloat(), 12.dp.toPx())
        } else 0f
        
        val animatedBarHeight = targetBarHeight * animationProgress
        
        // Calculate x position for proper centering
        val x = barSpacing + index * (barWidth + barSpacing)
        val y = size.height - animatedBarHeight
        
        if (animatedBarHeight > 0) {
            // Create bars with the theme color for better visibility
            val primaryColor = Color(0xFFE5FF7F) // Main theme color
            val accentColor = Color(0xFFD4F06B) // Slightly darker variant
            
            val gradient = Brush.verticalGradient(
                colors = listOf(primaryColor, accentColor),
                startY = y,
                endY = y + animatedBarHeight
            )
            
            // Draw shadow first for depth
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.15f),
                topLeft = Offset(x + 2.dp.toPx(), y + 2.dp.toPx()),
                size = Size(barWidth, animatedBarHeight),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            
            // Draw main bar with gradient
            drawRoundRect(
                brush = gradient,
                topLeft = Offset(x, y),
                size = Size(barWidth, animatedBarHeight),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            
            // Draw subtle highlight on top edge for modern look
            drawRoundRect(
                color = Color.White.copy(alpha = 0.3f),
                topLeft = Offset(x, y),
                size = Size(barWidth, 6.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
        } else {
            // Draw placeholder for days with no spending
            drawRoundRect(
                color = Color.White.copy(alpha = 0.15f),
                topLeft = Offset(x, size.height - 10.dp.toPx()),
                size = Size(barWidth, 10.dp.toPx()),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
        }
    }
}

private fun getDayAbbreviation(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}