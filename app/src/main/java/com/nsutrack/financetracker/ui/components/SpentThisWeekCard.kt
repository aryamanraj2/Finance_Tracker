package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.BarChart
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
        modifier = Modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5FF7F)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Spent this week",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontFamily = outfit,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
                Text(
                    text = "₹6,426.94",
                    fontSize = 32.sp,
                    fontFamily = outfit,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Higher",
                        tint = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "3% higher",
                        color = Color.Black.copy(alpha = 0.7f),
                        fontFamily = outfit,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.Outlined.BarChart,
                contentDescription = "Graph",
                tint = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier
                    .size(84.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-16).dp, y = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SpentThisWeekCardPreview() {
    FinanceTrackerTheme {
        SpentThisWeekCard()
    }
}
