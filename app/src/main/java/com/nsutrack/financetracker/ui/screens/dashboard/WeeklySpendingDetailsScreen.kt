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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.data.Transaction
import com.nsutrack.financetracker.ui.components.BarChart
import com.nsutrack.financetracker.ui.components.TransactionListItem
import com.nsutrack.financetracker.ui.utils.formatCurrency
import java.time.LocalDate
import kotlin.math.max
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import kotlinx.coroutines.delay

/**
 * Calculate dynamic font size based on text length to prevent overflow
 * Starts with base font size and reduces as text gets longer
 */
private fun calculateDynamicFontSize(text: String, baseFontSize: Float): Float {
    val baseLength = 8 // Base length for normal font size (e.g., "₹1,234")
    val textLength = text.length
    
    return when {
        textLength <= baseLength -> baseFontSize
        textLength <= baseLength + 2 -> baseFontSize * 0.9f // Slight reduction
        textLength <= baseLength + 4 -> baseFontSize * 0.8f // More reduction
        textLength <= baseLength + 6 -> baseFontSize * 0.7f // Significant reduction
        else -> max(baseFontSize * 0.6f, 20f) // Minimum readable size (slightly larger for this screen)
    }
}

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
            .background(Color(0xFF1C1C1E)) // Match dashboard background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Custom Top Bar - no animation
            TopAppBar(
                title = {
                    Text(
                        "Weekly Spending",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    WeekRangeHeader(startOfCurrentWeek, endOfCurrentWeek)
                }
                
                item {
                    WeeklySpendingCard(weeklySpending)
                }
                
                item {
                    // Bar chart card with modern styling
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color.Black.copy(alpha = 0.1f),
                                spotColor = Color.Black.copy(alpha = 0.1f)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2C2C2E) // Match dashboard dark card theme
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp)
                        ) {
                            Text(
                                text = "Daily Spending Overview",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )
                            
                            BarChart(
                                data = graphData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                            )
                        }
                    }
                }
                
                // Recent transactions card (instead of list)
                if (currentWeekTransactions.isNotEmpty()) {
                    item {
                        RecentTransactionsCard(
                            transactions = currentWeekTransactions.take(5) // Show only top 5
                        )
                    }
                }
                
                // Spacer at the end
                item {
                    Spacer(modifier = Modifier.height(40.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE5FF7F) // Match dashboard theme
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 300.dp.value
                    )
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Week of",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = startOfWeek.format(yearFormatter),
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WeeklySpendingCard(weeklySpending: WeeklySpendingSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE5FF7F) // Match dashboard theme
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        radius = 400.dp.value
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Spent This Week",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = formatCurrency(weeklySpending.currentWeekTotal),
                    color = Color.Black,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                )
                
                if (weeklySpending.percentageChange != 0.0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val isIncrease = weeklySpending.percentageChange > 0
                    val changeColor = if (isIncrease) {
                        Color(0xFFE53E3E) // Stronger red for increase
                    } else {
                        Color(0xFF38A169) // Stronger green for decrease  
                    }
                    val changeText = if (isIncrease) "+" else ""
                    val changeIcon = if (isIncrease) "↗" else "↘"
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = changeColor.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = changeIcon,
                                color = changeColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            Text(
                                text = "${changeText}${String.format("%.1f", kotlin.math.abs(weeklySpending.percentageChange))}% from last week",
                                color = changeColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentTransactionsCard(transactions: List<Transaction>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E) // Match dashboard dark cards
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${transactions.size} this week",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            transactions.forEachIndexed { index, transaction ->
                TransactionListItem(
                    transaction = transaction,
                    showDivider = index < transactions.size - 1
                )
                
                if (index < transactions.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
