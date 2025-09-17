package com.nsutrack.financetracker.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    var monthlyAllowance by remember { mutableStateOf("") }
    var monthlyExpenditure by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // Load initial values
    LaunchedEffect(Unit) {
        val currentAllowance = viewModel.getMonthlyAllowance(context)
        val currentExpenditure = viewModel.getMonthlySpend(context)
        monthlyAllowance = if (currentAllowance > 0) currentAllowance.toInt().toString() else ""
        monthlyExpenditure = if (currentExpenditure > 0) currentExpenditure.toInt().toString() else ""
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Financial Settings Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Financial Settings",
                            color = Yellow,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = outfit
                        )

                        // Monthly Allowance Field
                        Column {
                            Text(
                                text = "Monthly Allowance",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = outfit,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = monthlyAllowance,
                                onValueChange = { monthlyAllowance = it },
                                label = { Text("Enter amount", fontFamily = outfit) },
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Yellow,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Yellow,
                                    focusedLabelColor = Yellow,
                                    unfocusedLabelColor = Color.Gray
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                prefix = { Text("₹", color = Color.Gray) }
                            )
                        }

                        // Monthly Expenditure Field
                        Column {
                            Text(
                                text = "Monthly Expenditure",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = outfit,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = monthlyExpenditure,
                                onValueChange = { monthlyExpenditure = it },
                                label = { Text("Enter amount", fontFamily = outfit) },
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Yellow,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Yellow,
                                    focusedLabelColor = Yellow,
                                    unfocusedLabelColor = Color.Gray
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                prefix = { Text("₹", color = Color.Gray) }
                            )
                        }

                        // Current savings display
                        val allowanceValue = monthlyAllowance.toDoubleOrNull() ?: 0.0
                        val expenditureValue = monthlyExpenditure.toDoubleOrNull() ?: 0.0
                        val savingsAmount = allowanceValue - expenditureValue
                        val savingsPercentage = if (allowanceValue > 0) {
                            ((savingsAmount / allowanceValue) * 100).toInt()
                        } else 0

                        if (allowanceValue > 0 && expenditureValue > 0) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (savingsAmount >= 0) {
                                        Color(0xFF1B4D3E) // Dark green for positive savings
                                    } else {
                                        Color(0xFF4D1B1B) // Dark red for overspending
                                    }
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Monthly Savings",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        fontFamily = outfit
                                    )
                                    Text(
                                        text = "₹${savingsAmount.toInt()}",
                                        color = if (savingsAmount >= 0) Color.Green else Color.Red,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = outfit
                                    )
                                    Text(
                                        text = if (savingsAmount >= 0) {
                                            "$savingsPercentage% of allowance saved"
                                        } else {
                                            "${-savingsPercentage}% overspending"
                                        },
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        fontFamily = outfit
                                    )
                                }
                            }
                        }
                    }
                }

                // Save Button
                Button(
                    onClick = {
                        val allowanceValue = monthlyAllowance.toDoubleOrNull()
                        val expenditureValue = monthlyExpenditure.toDoubleOrNull()
                        
                        if (allowanceValue != null && expenditureValue != null) {
                            isLoading = true
                            viewModel.updateFinancialSettings(context, allowanceValue, expenditureValue)
                            isLoading = false
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Yellow,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.DarkGray
                    ),
                    enabled = !isLoading && 
                             monthlyAllowance.toDoubleOrNull() != null && 
                             monthlyExpenditure.toDoubleOrNull() != null
                ) {
                    Text(
                        text = if (isLoading) "Saving..." else "Save Changes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = outfit,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Help Text
                Text(
                    text = "Your changes will be reflected in your spending analysis and insights.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = outfit,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        onNavigateBack = {}
    )
}