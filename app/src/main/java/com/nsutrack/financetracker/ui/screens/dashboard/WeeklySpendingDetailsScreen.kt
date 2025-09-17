package com.nsutrack.financetracker.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.components.BarChart
import com.nsutrack.financetracker.ui.components.TransactionListItem
import com.nsutrack.financetracker.ui.utils.formatCurrency
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySpendingDetailsScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val graphData by viewModel.weeklyGraphData.collectAsState()
    val weeklySpending by viewModel.weeklySpendingSummary.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    
    // Only keep simple loading state for bar chart
    var isLoaded by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isLoaded = true
    }
    
    // Filter transactions for current week
    val today = LocalDate.now()
    val startOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val endOfCurrentWeek = startOfCurrentWeek.plusDays(6)
    val currentWeekTransactions = transactions.filter {
        !it.date.toLocalDate().isBefore(startOfCurrentWeek) &&
        !it.date.toLocalDate().isAfter(endOfCurrentWeek) &&
        it.type == "debit"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE5FF7F), // Top lime green
                        Color(0xFFF0FF8C)  // Bottom lighter green
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Custom Top Bar - no animation
            TopAppBar(
                title = {
                    Text(
                        "Weekly Spending",
                        color = Color(0xFF1C1C1E),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1C1C1E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    WeekRangeHeader(startOfCurrentWeek, endOfCurrentWeek)
                }
                
                item {
                    WeeklySpendingCard(weeklySpending)
                }
                
                item {
                    // Bar chart card - only this gets animation through the chart itself
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2C2C2E)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Daily Spending Chart",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                            
                            BarChart(
                                data = graphData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "This Week's Transactions",
                        color = Color(0xFF1C1C1E),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                // List of transactions - no animation
                items(currentWeekTransactions) { transaction ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3A3A3C)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        TransactionListItem(transaction)
                    }
                }
                
                // Spacer at the end
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun WeekRangeHeader(startOfWeek: LocalDate, endOfWeek: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd")
    val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF48484A)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.03f),
                            Color.Transparent,
                            Color.White.copy(alpha = 0.03f)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Week of",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = startOfWeek.format(yearFormatter),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WeeklySpendingCard(weeklySpending: WeeklySpendingSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3C3C3E)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.02f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Spent This Week",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = formatCurrency(weeklySpending.currentWeekTotal),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                
                if (weeklySpending.percentageChange != 0.0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val isIncrease = weeklySpending.percentageChange > 0
                    val changeColor = if (isIncrease) {
                        Color(0xFFFF6B6B) // Soft red for increase
                    } else {
                        Color(0xFF51CF66) // Soft green for decrease  
                    }
                    val changeText = if (isIncrease) "+" else ""
                    val changeIcon = if (isIncrease) "↗" else "↘"
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = changeIcon,
                            color = changeColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = "${changeText}${String.format("%.1f", kotlin.math.abs(weeklySpending.percentageChange))}% from last week",
                            color = changeColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
