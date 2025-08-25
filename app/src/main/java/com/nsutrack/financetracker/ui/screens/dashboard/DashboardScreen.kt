package com.nsutrack.financetracker.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.alpha
import com.nsutrack.financetracker.ui.components.CollapsingHeader
import com.nsutrack.financetracker.ui.utils.formatCurrency
import com.nsutrack.financetracker.ui.components.OperationsList
import com.nsutrack.financetracker.ui.components.SpentThisWeekCard
import com.nsutrack.financetracker.ui.components.GetInsightsButton
import com.nsutrack.financetracker.ui.components.Operation
import com.nsutrack.financetracker.ui.components.SubscriptionCard

@Composable
fun DashboardScreen() {
    val operations = listOf(
        Operation("Sean Kim", "Transfer", -320.00, "5.28 AM"),
        Operation("Apple Store", "Macbook Pro", -2320.00, "3.15 PM"),
        Operation("Netflix", "Subscription", -15.00, "8.30 AM"),
        Operation("Amazon", "Shopping", -120.00, "1.00 PM"),
        Operation("Starbucks", "Coffee", -5.00, "9.00 AM")
    )
    val top3Operations = operations.sortedBy { it.amount }.take(3)
    val operationColors = mapOf(
        top3Operations.getOrNull(0) to Color(0xFFE5DD00),
        top3Operations.getOrNull(1) to Color(0xFF82D85B),
        top3Operations.getOrNull(2) to Color.White
    )
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Spacer to push content below the initial header
            Spacer(modifier = Modifier.height(88.dp)) // Approx height of TopAppBar
            TotalBalance()
            Spacer(modifier = Modifier.height(24.dp))
            CardsSection()
            Spacer(modifier = Modifier.height(24.dp))
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

        // Initial header with icons that fades out
        val initialHeaderAlpha = animateFloatAsState(targetValue = if (scrollState.value > 150) 0f else 1f).value
        DashboardHeaderIcons(modifier = Modifier.alpha(initialHeaderAlpha))

        // Collapsing header that fades in
        CollapsingHeader(scrollOffset = scrollState.value.toFloat())
    }
}

@Composable
fun DashboardHeaderIcons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
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
fun TotalBalance() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(
            text = "All sources",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = "Total Balance",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                    append(formatCurrency(18360.22))
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
fun CardsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            SpentThisWeekCard()
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
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
