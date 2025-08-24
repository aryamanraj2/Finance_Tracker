package com.nsutrack.financetracker.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.nsutrack.financetracker.R
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme
import com.nsutrack.financetracker.ui.theme.Yellow

@Composable
fun GetInsightsButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Yellow.copy(alpha = 0.1f),
            contentColor = Yellow
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_gemini_logo),
            contentDescription = "Get Insights",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Get Insights")
    }
}

@Preview
@Composable
fun GetInsightsButtonPreview() {
    FinanceTrackerTheme {
        GetInsightsButton(onClick = {})
    }
}
