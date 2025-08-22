package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme
import com.nsutrack.financetracker.ui.theme.outfit

@Composable
fun SpentThisWeekCard() {
    Card(
        modifier = Modifier
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "Spent this week",
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = outfit,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
                Text(
                    text = "$6 426.94",
                    fontSize = 32.sp,
                    fontFamily = outfit,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Higher",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "3% higher",
                        color = Color.White.copy(alpha = 0.7f),
                        fontFamily = outfit,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
            PieChart(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
fun PieChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(48.dp)) {
        drawArc(
            color = Color.Gray,
            startAngle = 0f,
            sweepAngle = 120f,
            useCenter = true
        )
        drawArc(
            color = Color.DarkGray,
            startAngle = 120f,
            sweepAngle = 90f,
            useCenter = true
        )
        drawArc(
            color = Color.LightGray,
            startAngle = 210f,
            sweepAngle = 90f,
            useCenter = true
        )
        drawArc(
            color = Color.Black,
            startAngle = 300f,
            sweepAngle = 60f,
            useCenter = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpentThisWeekCardPreview() {
    FinanceTrackerTheme {
        SpentThisWeekCard()
    }
}