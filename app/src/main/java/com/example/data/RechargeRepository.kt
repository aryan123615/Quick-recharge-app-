package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object RechargeRepository {
    private val _walletBalance = MutableStateFlow(450.0)
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    private val _coins = MutableStateFlow(1250)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _transactions = MutableStateFlow(
        mutableListOf(
            Transaction(
                id = "TXN98432178",
                mobileNumber = "9876543210",
                operator = "Jio",
                circle = "Maharashtra & Goa",
                amount = 299,
                planDetails = "28 Days • 1.5 GB/day • Unlimited Calls",
                date = "24 Oct 2026, 02:30 PM",
                status = "SUCCESS",
                paymentMethod = "Google Pay UPI",
                cashbackEarned = 15
            ),
            Transaction(
                id = "TXN87654321",
                mobileNumber = "9123456789",
                operator = "Airtel",
                circle = "Mumbai",
                amount = 719,
                planDetails = "84 Days • 2 GB/day • Unlimited Calls",
                date = "20 Oct 2026, 11:15 AM",
                status = "SUCCESS",
                paymentMethod = "PhonePe UPI",
                cashbackEarned = 35
            ),
            Transaction(
                id = "TXN76543210",
                mobileNumber = "9988776655",
                operator = "Vi",
                circle = "Delhi",
                amount = 479,
                planDetails = "56 Days • 1.5 GB/day • Binge All Night",
                date = "15 Oct 2026, 09:45 AM",
                status = "SUCCESS",
                paymentMethod = "Paytm Wallet",
                cashbackEarned = 20
            )
        )
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _rewards = MutableStateFlow(
        mutableListOf(
            Reward("R1", "Scratch Card", "Win up to ₹100 cashback on next recharge", 50, "SCRATCH_CARD", false),
            Reward("R2", "Super Saver Card", "Guaranteed ₹25 cashback on BSNL / Vi", 25, "SCRATCH_CARD", false),
            Reward("R3", "Daily Streak Bonus", "Claim 100 free reward coins", 100, "COIN_BONUS", false),
            Reward("R4", "Welcome Voucher", "Flat ₹50 off on first Airtel recharge", 50, "CASHBACK", true)
        )
    )
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    fun getPlansForOperator(operator: String): List<RechargePlan> {
        val op = operator.ifBlank { "Jio" }
        return listOf(
            RechargePlan("p1", op, "Popular", 299, "28 Days", "1.5 GB/day", "Unlimited Calls + 100 SMS/day + Free Hello Tunes", true),
            RechargePlan("p2", op, "Popular", 719, "84 Days", "2.0 GB/day", "True 5G Unlimited Data + 100 SMS/day + Disney+ Hotstar", true),
            RechargePlan("p3", op, "Unlimited", 239, "24 Days", "1.5 GB/day", "Unlimited Calls + 100 SMS/day", false),
            RechargePlan("p4", op, "Unlimited", 2999, "365 Days", "2.5 GB/day", "Annual Pack with Unlimited 5G Data & Prime Video", true),
            RechargePlan("p5", op, "Data", 25, "1 Day", "2 GB", "High-speed 4G/5G data add-on pack", false),
            RechargePlan("p6", op, "Data", 195, "Active Plan", "8 GB", "Data voucher with no daily limits", false),
            RechargePlan("p7", op, "Cricket", 899, "90 Days", "3 GB/day", "Special IPL & Cricket season pack with SonyLiv access", true),
            RechargePlan("p8", op, "Talktime", 149, "28 Days", "1 GB total", "Budget-friendly plan with standard calling rates", false)
        )
    }

    fun addTransaction(txn: Transaction) {
        val currentList = _transactions.value.toMutableList()
        currentList.add(0, txn)
        _transactions.value = currentList
        _walletBalance.value = (_walletBalance.value - txn.amount + txn.cashbackEarned).coerceAtLeast(0.0)
        _coins.value += txn.cashbackEarned * 10
    }

    fun redeemReward(rewardId: String): Int {
        val current = _rewards.value.map {
            if (it.id == rewardId) it.copy(isRedeemed = true) else it
        }
        _rewards.value = current.toMutableList()
        val r = _rewards.value.find { it.id == rewardId }
        val valEarned = r?.value ?: 10
        _walletBalance.value += valEarned
        return valEarned
    }
}
