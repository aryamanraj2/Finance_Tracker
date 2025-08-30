package com.nsutrack.financetracker.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.nsutrack.financetracker.ui.components.GraphAnimation
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit
import com.nsutrack.financetracker.ui.utils.OnboardingManager
import kotlinx.coroutines.delay

sealed class Page {
    object Allowance : Page()
    data class Spend(val monthlyAllowance: Double) : Page()
    data class Loading(val category: SavingsCategory) : Page()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(onOnboardingFinished: () -> Unit) {
    var currentPage by remember { mutableStateOf<Page>(Page.Allowance) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        val context = LocalContext.current
        AnimatedContent(targetState = currentPage) { page ->
            when (page) {
                is Page.Allowance -> AllowancePage { allowance ->
                    currentPage = Page.Spend(allowance)
                }
                is Page.Spend -> SpendPage(monthlyAllowance = page.monthlyAllowance) { spend ->
                    val savingsPercentage = (1 - spend / page.monthlyAllowance) * 100
                    val category = when {
                        savingsPercentage < 15 -> SavingsCategory.A
                        savingsPercentage < 30 -> SavingsCategory.B
                        savingsPercentage < 60 -> SavingsCategory.C
                        else -> SavingsCategory.D
                    }
                    OnboardingManager.setMonthlyAllowance(context, page.monthlyAllowance)
                    OnboardingManager.setMonthlySpend(context, spend)
                    OnboardingManager.setOnboardingCompleted(context, true)
                    currentPage = Page.Loading(category)
                }
                is Page.Loading -> LoadingScreen(category = page.category) {
                    onOnboardingFinished()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen {}
}

@Composable
fun AllowancePage(onNext: (Double) -> Unit) {
    val fullText = "What's your monthly allowance?"
    var allowance by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = fullText,
            color = Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = outfit,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        OutlinedTextField(
            value = allowance,
            onValueChange = { allowance = it },
            label = { Text("Monthly Allowance", fontFamily = outfit) },
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
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val allowanceValue = allowance.toDoubleOrNull()
                if (allowanceValue != null) {
                    onNext(allowanceValue)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Yellow,
                disabledContainerColor = Color.DarkGray,
                contentColor = Color.Black,
                disabledContentColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = allowance.toDoubleOrNull() != null
        ) {
            Text(
                text = "Next",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = outfit
            )
        }
    }
}
