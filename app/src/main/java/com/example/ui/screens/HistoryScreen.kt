package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargeRepository
import com.example.data.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val transactions by RechargeRepository.transactions.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Success", "Jio", "Airtel", "Vi", "BSNL")

    val filteredTransactions = transactions.filter { txn ->
        when (selectedFilter) {
            "Success" -> txn.status == "SUCCESS"
            "Jio", "Airtel", "Vi", "BSNL" -> txn.operator.equals(selectedFilter, ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("history_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                filters.forEach { filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        text = { Text(filter, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("filter_${filter.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredTransactions) { txn ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("${txn.operator} • ${txn.mobileNumber}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text("ID: ${txn.id}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("₹${txn.amount}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (txn.status == "SUCCESS") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                    ) {
                                        Text(
                                            txn.status,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (txn.status == "SUCCESS") Color(0xFF2E7D32) else Color(0xFFC62828),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(txn.planDetails, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(txn.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Paid via ${txn.paymentMethod}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
