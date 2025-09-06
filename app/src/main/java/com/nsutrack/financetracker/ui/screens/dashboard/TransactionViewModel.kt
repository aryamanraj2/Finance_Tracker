package com.nsutrack.financetracker.ui.screens.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nsutrack.financetracker.data.SmsDataSource
import com.nsutrack.financetracker.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    init {
        loadTransactions()
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

data class DailySummary(
    val totalCredit: Double,
    val totalDebit: Double,
    val netBalance: Double
)