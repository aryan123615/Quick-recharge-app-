package com.example.data

data class RechargePlan(
    val id: String,
    val operator: String, // Jio, Airtel, Vi, BSNL
    val category: String, // Popular, Unlimited, Data, Talktime, Cricket
    val price: Int,
    val validity: String,
    val data: String,
    val description: String,
    val isPopular: Boolean = false
)

data class Transaction(
    val id: String,
    val mobileNumber: String,
    val operator: String,
    val circle: String,
    val amount: Int,
    val planDetails: String,
    val date: String,
    val status: String, // SUCCESS, PENDING, FAILED
    val paymentMethod: String,
    val cashbackEarned: Int
)

data class Reward(
    val id: String,
    val title: String,
    val description: String,
    val value: Int,
    val type: String, // SCRATCH_CARD, COIN_BONUS, CASHBACK
    var isRedeemed: Boolean = false
)

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val walletBalance: Double,
    val coins: Int,
    val scratchCardsCount: Int
)
