package com.nsutrack.financetracker.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.components.GetInsightsButton
import com.nsutrack.financetracker.ui.components.IosInspiredHeader
import com.nsutrack.financetracker.ui.components.Operation
import com.nsutrack.financetracker.ui.components.OperationsList
import com.nsutrack.financetracker.ui.components.SpentThisWeekCard
import com.nsutrack.financetracker.ui.components.SubscriptionCard
import com.nsutrack.financetracker.ui.utils.formatCurrency
import com.nsutrack.financetracker.ui.screens.dashboard.WeeklySpendingSummary

@Composable
fun DashboardScreen(
    viewModel: TransactionViewModel,
    onNavigateToWeeklySpendingDetails: () -> Unit,
    onNavigateToChat: () -> Unit = {}
) {
    val transactions by viewModel.todaysTransactions.collectAsState()
    val dailySummary by viewModel.dailySummary.collectAsState()
    val weeklySummary by viewModel.weeklySpendingSummary.collectAsState()

    val operations = transactions.map {
        Operation(
            name = it.upiId ?: "Unknown",
            category = it.type,
            amount = it.amount,
            time = it.date.toLocalTime().toString()
        )
    }
    val top3Operations = operations.sortedBy { it.amount }.take(3)
    val operationColors = mapOf(
        top3Operations.getOrNull(0) to Color(0xFFE5DD00),
        top3Operations.getOrNull(1) to Color(0xFF82D85B),
        top3Operations.getOrNull(2) to Color.White
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        IosInspiredHeader {
            item {
                Column {
                    TopAppBar()
                    Spacer(modifier = Modifier.height(24.dp))
                    TotalBalance(dailySummary)
                    Spacer(modifier = Modifier.height(24.dp))
                    CardsSection(
                        weeklySummary = weeklySummary,
                        onNavigateToWeeklySpendingDetails = onNavigateToWeeklySpendingDetails,
                        onNavigateToChat = onNavigateToChat
                    )
                    Spacer(modifier = Modifier.height(24.dp))
//                    DailySummaryCard(dailySummary)
//                    Spacer(modifier = Modifier.height(24.dp))
                    OperationsList(operations = operations, operationColors = operationColors)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Made with ♥ by aryaman",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(40.dp),
            tint = Color.White
        )
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
            modifier = Modifier.size(40.dp),
            tint = Color.White
        )
    }
}

@Composable
fun TotalBalance(dailySummary: DailySummary) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "All sources",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = "Remaining",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                    append(formatCurrency(dailySummary.netBalance))
                }
                append(" ")
                withStyle(style = SpanStyle(color = Color.Gray, fontSize = 32.sp, fontWeight = FontWeight.Normal)) {
                    append("rupees")
                }
            }
        )
    }
}

@Composable
fun CardsSection(
    weeklySummary: WeeklySpendingSummary,
    onNavigateToWeeklySpendingDetails: () -> Unit,
    onNavigateToChat: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            SpentThisWeekCard(
                weeklySpendingSummary = weeklySummary,
                onClick = onNavigateToWeeklySpendingDetails
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SubscriptionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            GetInsightsButton(
                onClick = onNavigateToChat,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        viewModel = viewModel(),
        onNavigateToWeeklySpendingDetails = {},
        onNavigateToChat = {}
    )
}

//@Composable
//fun DailySummaryCard(dailySummary: DailySummary) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = "Today's Summary", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(16.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(text = "Credited", color = Color.Gray)
//                    Text(text = formatCurrency(dailySummary.totalCredit), color = Color.Green, fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                }
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(text = "Debited", color = Color.Gray)
//                    Text(text = formatCurrency(dailySummary.totalDebit), color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    }
//}
