package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsutrack.financetracker.ui.screens.subscription.SubscriptionViewModel
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.utils.formatCurrency

@Composable
fun SubscriptionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    viewModel: SubscriptionViewModel = viewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    val latestSubscription = subscriptions.firstOrNull()
    
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Yellow),
                contentAlignment = Alignment.Center
            ) {
                if (latestSubscription != null) {
                    Text(
                        text = latestSubscription.name.take(1).uppercase(),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            if (latestSubscription != null) {
                Text("Subscription", fontWeight = FontWeight.Bold, color = Color.White)
                Text(latestSubscription.name, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    formatCurrency(latestSubscription.amount),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            } else {
                Text("Subscription", fontWeight = FontWeight.Bold, color = Color.White)
                Text("Tap to add", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Add subscriptions",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
