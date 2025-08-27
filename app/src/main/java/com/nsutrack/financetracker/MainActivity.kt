package com.nsutrack.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import com.nsutrack.financetracker.ui.screens.dashboard.DashboardScreen
import com.nsutrack.financetracker.ui.screens.onboarding.OnboardingScreen
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showOnboarding by remember { mutableStateOf(true) }

            FinanceTrackerTheme {
                AnimatedContent(
                    targetState = showOnboarding,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth }.togetherWith(
                            slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth })
                    }
                ) { show ->
                    if (show) {
                        OnboardingScreen {
                            showOnboarding = false
                        }
                    } else {
                        DashboardScreen()
                    }
                }
            }
        }
    }
}
