package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val rewards by RechargeRepository.rewards.collectAsState()
    val coins by RechargeRepository.coins.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rewards & Cashback", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("rewards_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Reward Coins", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            Text("$coins Coins", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Worth ₹${coins / 10}.00 toward free recharges", style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = {
                                if (coins >= 500) {
                                    Toast.makeText(context, "Successfully redeemed 500 coins for ₹50 wallet cash!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Need at least 500 coins to redeem", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("redeem_coins_button")
                        ) {
                            Text("Redeem")
                        }
                    }
                }
            }

            item {
                Text("Scratch Cards & Offers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(rewards) { reward ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (reward.isRedeemed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (reward.type == "SCRATCH_CARD") Icons.Default.CardGiftcard else Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(reward.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(reward.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        if (reward.isRedeemed) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE0E0E0)
                            ) {
                                Text(
                                    "Claimed",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    val amt = RechargeRepository.redeemReward(reward.id)
                                    Toast.makeText(context, "Unlocked ₹$amt cashback added to wallet!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("claim_reward_${reward.id}")
                            ) {
                                Text("Scratch & Win")
                            }
                        }
                    }
                }
            }
        }
    }
}
