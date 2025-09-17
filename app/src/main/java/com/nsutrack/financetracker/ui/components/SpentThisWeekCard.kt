package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowDownward
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
import com.nsutrack.financetracker.ui.screens.dashboard.WeeklySpendingSummary
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme
import com.nsutrack.financetracker.ui.theme.outfit
import com.nsutrack.financetracker.ui.utils.formatCurrency

@Composable
fun SpentThisWeekCard(
    weeklySpendingSummary: WeeklySpendingSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5FF7F)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
                    text = formatCurrency(weeklySpendingSummary.currentWeekTotal),
                    fontSize = 32.sp,
                    fontFamily = outfit,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val percentageChange = weeklySpendingSummary.percentageChange
                    val isIncrease = percentageChange >= 0
                    val arrowIcon = if (isIncrease) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                    val changeText = String.format("%.0f%% %s",
                        if(isIncrease) percentageChange else -percentageChange,
                        if(isIncrease) "higher" else "lower"
                    )

                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = if (isIncrease) "Higher" else "Lower",
                        tint = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = changeText,
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
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 16.dp, y = 32.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SpentThisWeekCardPreview() {
    FinanceTrackerTheme {
        SpentThisWeekCard(
            weeklySpendingSummary = WeeklySpendingSummary(
                currentWeekTotal = 6426.94,
                previousWeekTotal = 6239.75,
                percentageChange = 3.0
            ),
            onClick = {}
        )
    }
}
