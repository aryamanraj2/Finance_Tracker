package com.nsutrack.financetracker.ui.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nsutrack.financetracker.ui.components.BarChart
import com.nsutrack.financetracker.ui.components.TransactionListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySpendingDetailsScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val graphData = viewModel.getWeeklySpendingGraphData()
    val transactions = viewModel.transactions.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Spending") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Bar chart for weekly spending
            BarChart(
                data = graphData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            )

            // List of transactions for the current week
            LazyColumn {
                items(transactions) { transaction ->
                    TransactionListItem(transaction)
                }
            }
        }
    }
}
