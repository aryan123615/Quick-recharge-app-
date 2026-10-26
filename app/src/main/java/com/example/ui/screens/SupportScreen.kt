package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var queryText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("support_back_button")) {
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("24/7 Customer Assistance", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Facing recharge failure or payment debit issues? Our support desk responds in under 2 minutes.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { Toast.makeText(context, "Connected to Live Support Agent", Toast.LENGTH_SHORT).show() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("live_chat_button")
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Live Chat")
                        }
                    }
                }
            }

            item {
                Text("Frequently Asked Questions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                FaqItem(
                    question = "Recharge is successful but not credited to operator?",
                    answer = "Telecom operators usually credit prepaid recharges within 5-10 minutes. In rare cases of operator outages, it takes up to 2 hours. If unsuccessful, automatic refund is processed to your original payment method in 3-5 business days."
                )
            }

            item {
                FaqItem(
                    question = "How do I claim reward scratch cards?",
                    answer = "Every successful recharge unlocks a scratch card in the Rewards section. Scratch the card to reveal instant cashback credited directly to your Quick Recharge wallet."
                )
            }

            item {
                FaqItem(
                    question = "Are payments secure on Vivo Y35 app?",
                    answer = "Yes! All payments use bank-grade 256-bit encryption and comply fully with RBI guidelines via secure UPI and Card gateways."
                )
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(answer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
