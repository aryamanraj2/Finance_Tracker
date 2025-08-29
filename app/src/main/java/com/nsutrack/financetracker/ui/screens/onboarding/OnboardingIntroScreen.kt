package com.nsutrack.financetracker.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.ui.components.GraphAnimation
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit

@Composable
fun OnboardingIntroScreen(onFinished: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GraphAnimation()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "where all your financial trouble end",
                    color = Yellow,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(64.dp))
                Button(
                    onClick = onFinished,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Yellow,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
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
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingIntroScreenPreview() {
    OnboardingIntroScreen {}
}
