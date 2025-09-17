package com.nsutrack.financetracker.ui.screens.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsutrack.financetracker.data.Subscription
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    onNavigateBack: () -> Unit,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Subscriptions",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            // Content
            if (subscriptions.isEmpty()) {
                EmptySubscriptionsState(
                    onAddSubscription = { showAddDialog = true },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                SubscriptionsContent(
                    subscriptions = subscriptions,
                    onAddSubscription = { showAddDialog = true },
                    onRemoveSubscription = viewModel::removeSubscription
                )
            }
        }
    }
    
    // Add Subscription Dialog
    if (showAddDialog) {
        AddSubscriptionDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, amount ->
                viewModel.addSubscription(name, amount)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun EmptySubscriptionsState(
    onAddSubscription: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty state icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2C2E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Subscriptions Yet",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add your monthly subscriptions to track recurring expenses and get better spending insights",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onAddSubscription,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Yellow
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add Subscription",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SubscriptionsContent(
    subscriptions: List<Subscription>,
    onAddSubscription: () -> Unit,
    onRemoveSubscription: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Total Monthly Amount Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Monthly",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(subscriptions.sumOf { it.amount }),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Add Subscription Button
        Button(
            onClick = onAddSubscription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Yellow
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add New Subscription",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        // Subscriptions List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subscriptions, key = { it.id }) { subscription ->
                SubscriptionItem(
                    subscription = subscription,
                    onRemove = { onRemoveSubscription(subscription.id) }
                )
            }
        }
    }
}

@Composable
fun SubscriptionItem(
    subscription: Subscription,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Subscription Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Yellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subscription.name.take(1).uppercase(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Subscription Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subscription.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Monthly",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            // Amount and Delete Button
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatCurrency(subscription.amount),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddSubscriptionDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2C2C2E),
        title = {
            Text(
                text = "Add Subscription",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Subscription Name", color = Color.Gray) },
                    placeholder = { Text("e.g., Netflix, Spotify", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Yellow,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Yellow
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monthly Amount", color = Color.Gray) },
                    placeholder = { Text("₹99", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Yellow,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Yellow
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (name.isNotBlank() && amountDouble != null && amountDouble > 0) {
                        onAdd(name, amountDouble)
                        keyboardController?.hide()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow
                )
            ) {
                Text(
                    text = "Add",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    keyboardController?.hide()
                }
            ) {
                Text(
                    text = "Cancel",
                    color = Color.Gray
                )
            }
        }
    )
}