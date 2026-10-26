package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Auth) }

                    when (val screen = currentScreen) {
                        is Screen.Auth -> {
                            AuthScreen(
                                onLoginSuccess = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.Home -> {
                            BackHandler { finish() }
                            HomeScreen(
                                onNavigateToPlans = { op, num -> currentScreen = Screen.PlanBrowser(op, num) },
                                onNavigateToHistory = { currentScreen = Screen.History },
                                onNavigateToRewards = { currentScreen = Screen.Rewards },
                                onNavigateToAdmin = { currentScreen = Screen.Admin },
                                onNavigateToSupport = { currentScreen = Screen.Support }
                            )
                        }
                        is Screen.PlanBrowser -> {
                            BackHandler { currentScreen = Screen.Home }
                            PlanBrowserScreen(
                                operator = screen.operator,
                                mobileNumber = screen.mobileNumber,
                                onBack = { currentScreen = Screen.Home },
                                onSelectPlan = { plan -> currentScreen = Screen.Payment(screen.operator, screen.mobileNumber, plan.id) }
                            )
                        }
                        is Screen.Payment -> {
                            BackHandler { currentScreen = Screen.PlanBrowser(screen.operator, screen.mobileNumber) }
                            PaymentScreen(
                                operator = screen.operator,
                                mobileNumber = screen.mobileNumber,
                                planId = screen.planId,
                                onBack = { currentScreen = Screen.PlanBrowser(screen.operator, screen.mobileNumber) },
                                onPaymentSuccess = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.Rewards -> {
                            BackHandler { currentScreen = Screen.Home }
                            RewardsScreen(
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.History -> {
                            BackHandler { currentScreen = Screen.Home }
                            HistoryScreen(
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.Admin -> {
                            BackHandler { currentScreen = Screen.Home }
                            AdminPanelScreen(
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.Support -> {
                            BackHandler { currentScreen = Screen.Home }
                            SupportScreen(
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}
