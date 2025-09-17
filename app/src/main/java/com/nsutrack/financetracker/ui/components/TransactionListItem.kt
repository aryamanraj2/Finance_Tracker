package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nsutrack.financetracker.data.Transaction
import java.time.format.DateTimeFormatter

@Composable
fun TransactionListItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = transaction.upiId ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = transaction.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Text(
            text = "₹${transaction.amount}",
            style = MaterialTheme.typography.bodyLarge,
            color = if (transaction.type == "debit") Color.Red else Color.Green
        )
    }
}