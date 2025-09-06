package com.nsutrack.financetracker.data

import java.time.LocalDateTime

data class Transaction(
    val amount: Double,
    val type: String, // "credit" or "debit"
    val date: LocalDateTime,
    val upiId: String?,
    val rawMessage: String
)