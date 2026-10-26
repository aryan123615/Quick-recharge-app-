package com.example.ui.screens

sealed class Screen {
    object Auth : Screen()
    object Home : Screen()
    data class PlanBrowser(val operator: String, val mobileNumber: String) : Screen()
    data class Payment(val operator: String, val mobileNumber: String, val planId: String) : Screen()
    object Rewards : Screen()
    object History : Screen()
    object Admin : Screen()
    object Support : Screen()
}
