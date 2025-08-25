package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.utils.formatCurrency

data class Operation(
    val name: String,
    val description: String,
    val amount: Double,
    val time: String
)

@Composable
fun OperationsList(operations: List<Operation>, operationColors: Map<Operation?, Color>) {
    val totalSpent = operations.sumOf { it.amount }

    val segments = operations.map {
        (it.amount / totalSpent).toFloat() to (operationColors[it] ?: Color(0xFF656565))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Operation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = formatCurrency(totalSpent),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Spent this day", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            SegmentedProgressBar(
                segments = segments,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            operations.forEach { operation ->
                OperationItem(operation = operation, color = operationColors[operation] ?: Color(0xFF656565))
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OperationItem(operation: Operation, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(operation.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(operation.description, color = Color.Gray)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatCurrency(operation.amount), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(operation.time, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun OperationsListPreview() {
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
    OperationsList(operations = operations, operationColors = operationColors)
}
