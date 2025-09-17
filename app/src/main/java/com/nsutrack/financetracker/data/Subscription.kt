package com.nsutrack.financetracker.data

import java.time.LocalDateTime

data class Subscription(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val amount: Double,
    val createdAt: LocalDateTime = LocalDateTime.now()
)