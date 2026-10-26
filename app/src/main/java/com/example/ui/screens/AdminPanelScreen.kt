package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var broadcastMsg by remember { mutableStateOf("") }
    val transactions by RechargeRepository.transactions.collectAsState()

    val totalVolume = transactions.sumOf { it.amount }
    val totalCount = transactions.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin & Operator Console", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
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
                Text("System Metrics & Analytics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(title = "Total Volume", value = "₹$totalVolume", modifier = Modifier.weight(1f))
                    MetricCard(title = "Total Recharges", value = "$totalCount", modifier = Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(title = "Active Users", value = "14,820", modifier = Modifier.weight(1f))
                    MetricCard(title = "Success Rate", value = "99.8%", modifier = Modifier.weight(1f))
                }
            }

            item {
                Text("Broadcast Offer / Announcement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = broadcastMsg,
                            onValueChange = { broadcastMsg = it },
                            label = { Text("Broadcast Push Notification to Users") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("broadcast_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (broadcastMsg.isNotBlank()) {
                                    Toast.makeText(context, "Broadcast sent to all active users successfully!", Toast.LENGTH_SHORT).show()
                                    broadcastMsg = ""
                                } else {
                                    Toast.makeText(context, "Please enter message", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("send_broadcast_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Broadcast")
                        }
                    }
                }
            }

            item {
                Text("Telecom Gateway Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GatewayStatusRow(operator = "Jio API Gateway", status = "Connected (Latency: 42ms)")
                        GatewayStatusRow(operator = "Airtel Direct API", status = "Connected (Latency: 38ms)")
                        GatewayStatusRow(operator = "Vi Enterprise API", status = "Connected (Latency: 55ms)")
                        GatewayStatusRow(operator = "BSNL Sanchar API", status = "Connected (Latency: 82ms)")
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun GatewayStatusRow(operator: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(operator, fontWeight = FontWeight.Medium)
        Text(status, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
    }
}
