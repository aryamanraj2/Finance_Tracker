package com.nsutrack.financetracker.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.filled.ArrowUpward
import com.nsutrack.financetracker.ui.theme.outfit
import com.nsutrack.financetracker.ui.components.OperationCard
import com.nsutrack.financetracker.ui.components.SpentThisWeekCard

@Composable
fun DashboardScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1E))
        ) {
            TopAppBar()
            Spacer(modifier = Modifier.height(24.dp))
            TotalBalance()
            Spacer(modifier = Modifier.height(24.dp))
            CardsSection()
            Spacer(modifier = Modifier.height(24.dp))
            OperationCard()
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(40.dp),
            tint = Color.White
        )
        Row {
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Wallets", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Menu", color = Color.White)
            }
        }
    }
}

@Composable
fun TotalBalance() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(
            text = "All sources",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = "Total Balance",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("**** 3911", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("**** C3e3", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "18 360.22 usd",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CardsSection() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        // Spent this week card
        Box(modifier = Modifier.weight(1f)) {
            SpentThisWeekCard()
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Subscription card
        Card(
            modifier = Modifier
                .weight(1f)
                .height(180.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for Dribbble icon
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Subscription", fontWeight = FontWeight.Bold, color = Color.White)
                Text("Dribbble", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$8.00", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
