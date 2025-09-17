package com.nsutrack.financetracker.ui.screens.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nsutrack.financetracker.data.SmsDataSource
import com.nsutrack.financetracker.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val smsDataSource = SmsDataSource(application)

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _todaysTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val todaysTransactions: StateFlow<List<Transaction>> = _todaysTransactions

    private val _creditedTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val creditedTransactions: StateFlow<List<Transaction>> = _creditedTransactions

    private val _debitedTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val debitedTransactions: StateFlow<List<Transaction>> = _debitedTransactions

    private val _dailySummary = MutableStateFlow(DailySummary(0.0, 0.0, 0.0))
    val dailySummary: StateFlow<DailySummary> = _dailySummary
 
    private val _weeklySpendingSummary = MutableStateFlow(WeeklySpendingSummary())
    val weeklySpendingSummary: StateFlow<WeeklySpendingSummary> = _weeklySpendingSummary
 
     init {
         loadTransactions()
        loadWeeklySpending()
     }
 
    private fun loadWeeklySpending() {
        viewModelScope.launch {
            val allTransactions = smsDataSource.readSms()
            val today = LocalDate.now()
 
            // Calculate current week's spending (Sunday to Saturday)
            val startOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            val endOfCurrentWeek = startOfCurrentWeek.plusDays(6)
            val currentWeekTransactions = allTransactions.filter {
                !it.date.toLocalDate().isBefore(startOfCurrentWeek) && !it.date.toLocalDate().isAfter(endOfCurrentWeek) && it.type == "debit"
            }
            val currentWeekTotal = currentWeekTransactions.sumOf { it.amount }
 
            // Calculate previous week's spending
            val startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1)
            val endOfPreviousWeek = endOfCurrentWeek.minusWeeks(1)
            val previousWeekTransactions = allTransactions.filter {
                !it.date.toLocalDate().isBefore(startOfPreviousWeek) && !it.date.toLocalDate().isAfter(endOfPreviousWeek) && it.type == "debit"
            }
            val previousWeekTotal = previousWeekTransactions.sumOf { it.amount }
 
            // Calculate percentage change
            val percentageChange = if (previousWeekTotal > 0) {
                ((currentWeekTotal - previousWeekTotal) / previousWeekTotal) * 100
            } else {
                0.0
            }
 
            _weeklySpendingSummary.value = WeeklySpendingSummary(currentWeekTotal, previousWeekTotal, percentageChange)
        }
    }
 
    fun getWeeklySpendingGraphData(): Map<DayOfWeek, Double> {
        val today = LocalDate.now()
        val startOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endOfCurrentWeek = startOfCurrentWeek.plusDays(6)
        val currentWeekTransactions = _transactions.value.filter {
            !it.date.toLocalDate().isBefore(startOfCurrentWeek) && !it.date.toLocalDate().isAfter(endOfCurrentWeek) && it.type == "debit"
        }
 
        return currentWeekTransactions
            .groupBy { it.date.dayOfWeek }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
     private fun loadTransactions() {
         viewModelScope.launch {
             val allTransactions = smsDataSource.readSms()
             _transactions.value = allTransactions
 
             val today = LocalDate.now()
             val todaysTransactions = allTransactions.filter { it.date.toLocalDate() == today }
             _todaysTransactions.value = todaysTransactions
 
             val credited = todaysTransactions.filter { it.type == "credit" }
             val debited = todaysTransactions.filter { it.type == "debit" }
 
             _creditedTransactions.value = credited
             _debitedTransactions.value = debited
 
             val totalCredit = credited.sumOf { it.amount }
             val totalDebit = debited.sumOf { it.amount }
             val netBalance = totalCredit - totalDebit
 
             _dailySummary.value = DailySummary(totalCredit, totalDebit, netBalance)
         }
     }
 }
 
data class WeeklySpendingSummary(
    val currentWeekTotal: Double = 0.0,
    val previousWeekTotal: Double = 0.0,
    val percentageChange: Double = 0.0
)
 data class DailySummary(
     val totalCredit: Double,
     val totalDebit: Double,
     val netBalance: Double
 )