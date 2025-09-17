@file:Suppress("DEPRECATION")

package com.nsutrack.financetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Tasks
import com.nsutrack.financetracker.ui.screens.auth.AuthScreen
import com.nsutrack.financetracker.ui.screens.dashboard.DashboardScreen
import com.nsutrack.financetracker.ui.screens.dashboard.WeeklySpendingDetailsScreen
import com.nsutrack.financetracker.ui.screens.intro.IntroScreen
import com.nsutrack.financetracker.ui.screens.chat.ChatScreen
import com.nsutrack.financetracker.ui.screens.onboarding.OnboardingIntroScreen
import com.nsutrack.financetracker.ui.screens.onboarding.OnboardingScreen
import com.nsutrack.financetracker.ui.screens.subscription.SubscriptionScreen
import com.nsutrack.financetracker.ui.screens.settings.SettingsScreen
import com.nsutrack.financetracker.ui.theme.FinanceTrackerTheme
import com.nsutrack.financetracker.ui.utils.GoogleAuthUiClient
import com.nsutrack.financetracker.ui.utils.OnboardingManager
import kotlinx.coroutines.launch

sealed class Screen {
    object Intro : Screen()
    object OnboardingIntro : Screen()
    object Auth : Screen()
    object Onboarding : Screen()
    object Dashboard : Screen()
    object WeeklySpendingDetails : Screen()
    object Chat : Screen()
    object Subscription : Screen()
    object Settings : Screen()
}

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient: GoogleAuthUiClient by lazy {
        GoogleAuthUiClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Intro) }

            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                Toast.makeText(this, "SMS permission denied. Transaction features will be unavailable.", Toast.LENGTH_LONG).show()
            }
        }

            SideEffect {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_SMS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                    }
                    else -> {
                        // You can directly ask for the permission.
                        requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
                    }
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val account = Tasks.await(GoogleSignIn.getSignedInAccountFromIntent(result.data))
                        if (account != null) {
                            currentScreen = if (OnboardingManager.isOnboardingCompleted(context)) {
                                Screen.Dashboard
                            } else {
                                Screen.Onboarding
                            }
                        }
                    }
                }
            }

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
                            currentScreen = if (OnboardingManager.isOnboardingCompleted(context)) {
                                Screen.Dashboard
                            } else {
                                Screen.OnboardingIntro
                            }
                        }
                        is Screen.OnboardingIntro -> OnboardingIntroScreen {
                            currentScreen = if (googleAuthUiClient.getSignedInUser() != null) {
                                Screen.Onboarding
                            } else {
                                Screen.Auth
                            }
                        }
                        is Screen.Auth -> AuthScreen(
                            onGoogleSignIn = { launcher.launch(googleAuthUiClient.getSignInIntent()) },
                            onSkip = { currentScreen = Screen.Onboarding }
                        )
                        is Screen.Onboarding -> OnboardingScreen { currentScreen = Screen.Dashboard }
                        is Screen.Dashboard -> {
                            val viewModel: com.nsutrack.financetracker.ui.screens.dashboard.TransactionViewModel = viewModel()
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToWeeklySpendingDetails = {
                                    currentScreen = Screen.WeeklySpendingDetails
                                },
                                onNavigateToChat = {
                                    currentScreen = Screen.Chat
                                },
                                onNavigateToSubscription = {
                                    currentScreen = Screen.Subscription
                                },
                                onNavigateToSettings = {
                                    currentScreen = Screen.Settings
                                }
                            )
                        }
                        is Screen.WeeklySpendingDetails -> {
                            val viewModel: com.nsutrack.financetracker.ui.screens.dashboard.TransactionViewModel = viewModel()
                            WeeklySpendingDetailsScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    currentScreen = Screen.Dashboard
                                }
                            )
                        }
                        is Screen.Chat -> {
                            ChatScreen(
                                onBackClick = {
                                    currentScreen = Screen.Dashboard
                                }
                            )
                        }
                        is Screen.Subscription -> {
                            SubscriptionScreen(
                                onNavigateBack = {
                                    currentScreen = Screen.Dashboard
                                }
                            )
                        }
                        is Screen.Settings -> {
                            SettingsScreen(
                                onNavigateBack = {
                                    currentScreen = Screen.Dashboard
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
