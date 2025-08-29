package com.nsutrack.financetracker.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.nsutrack.financetracker.ui.theme.Yellow
import com.nsutrack.financetracker.ui.theme.outfit
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AuthScreen(onGoogleSignIn: () -> Unit, onSkip: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Join the revolution",
                color = Yellow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = outfit,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                onClick = onGoogleSignIn,
                colors = ButtonDefaults.buttonColors(containerColor = Yellow),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Join with Google",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Implement GitHub sign-in */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Join with GitHub",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSkip,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Skip for now",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = outfit
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(onGoogleSignIn = {}, onSkip = {})
}
