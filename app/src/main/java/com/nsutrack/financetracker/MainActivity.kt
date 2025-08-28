package com.nsutrack.financetracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import com.nsutrack.financetracker.ui.screens.dashboard.DashboardScreen
import com.nsutrack.financetracker.ui.screens.intro.IntroScreen
import com.nsutrack.financetracker.ui.screens.onboarding.OnboardingScreen
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme
import com.nsutrack.financetracker.ui.utils.OnboardingManager

sealed class Screen {
    object Intro : Screen()
    object Onboarding : Screen()
    object Dashboard : Screen()
}

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Intro) }

            FinanceTrackerTheme {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(500)) { fullWidth -> fullWidth }.togetherWith(
                            slideOutHorizontally(animationSpec = tween(500)) { fullWidth -> -fullWidth })
                    }
                ) { screen ->
                    when (screen) {
                        is Screen.Intro -> IntroScreen {
                            currentScreen =
                                if (OnboardingManager.isOnboardingCompleted(context)) Screen.Dashboard else Screen.Onboarding
                        }
                        is Screen.Onboarding -> OnboardingScreen { currentScreen = Screen.Dashboard }
                        is Screen.Dashboard -> DashboardScreen()
                    }
                }
            }
        }
    }
}
