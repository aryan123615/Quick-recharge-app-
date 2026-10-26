package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlans: (String, String) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToRewards: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    var mobileNumber by remember { mutableStateOf("9876543210") }
    var selectedOperator by remember { mutableStateOf("Jio") }
    var selectedCircle by remember { mutableStateOf("Maharashtra & Goa") }
    var expandedCircle by remember { mutableStateOf(false) }

    val walletBalance by RechargeRepository.walletBalance.collectAsState()
    val coins by RechargeRepository.coins.collectAsState()
    val transactions by RechargeRepository.transactions.collectAsState()

    val circles = listOf("Maharashtra & Goa", "Mumbai", "Delhi", "Karnataka", "Tamil Nadu", "Uttar Pradesh", "Gujarat", "Kolkata")
    val operators = listOf("Jio", "Airtel", "Vi", "BSNL")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Quick Recharge India", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Vivo Y35 • Fast & Secure", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAdmin, modifier = Modifier.testTag("admin_icon_button")) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Panel", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onNavigateToSupport, modifier = Modifier.testTag("support_icon_button")) {
                        Icon(Icons.Default.SupportAgent, contentDescription = "Support", tint = MaterialTheme.colorScheme.primary)
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
            // Wallet & Rewards Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Wallet Balance", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("₹${String.format("%.2f", walletBalance)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("$coins Coins Available", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Button(
                            onClick = onNavigateToRewards,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("rewards_button")
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rewards")
                        }
                    }
                }
            }

            // Quick Recharge Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Mobile Recharge", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10) mobileNumber = it },
                            label = { Text("Mobile Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { /* Contact picker simulation */ }) {
                                    Icon(Icons.Default.Contacts, contentDescription = "Pick Contact")
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mobile_number_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Operator selection
                        Column {
                            Text("Select Operator", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                operators.forEach { op ->
                                    val isSelected = selectedOperator == op
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedOperator = op },
                                        label = { Text(op, fontWeight = FontWeight.Bold) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                        } else null,
                                        modifier = Modifier.testTag("operator_${op.lowercase()}")
                                    )
                                }
                            }
                        }

                        // Circle selection dropdown
                        ExposedDropdownMenuBox(
                            expanded = expandedCircle,
                            onExpandedChange = { expandedCircle = !expandedCircle }
                        ) {
                            OutlinedTextField(
                                value = selectedCircle,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Telecom Circle / State") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCircle) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("circle_dropdown"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCircle,
                                onDismissRequest = { expandedCircle = false }
                            ) {
                                circles.forEach { circle ->
                                    DropdownMenuItem(
                                        text = { Text(circle) },
                                        onClick = {
                                            selectedCircle = circle
                                            expandedCircle = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (mobileNumber.length == 10) {
                                    onNavigateToPlans(selectedOperator, mobileNumber)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("browse_plans_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Browse Plans & Recharge", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick Service Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ServiceShortcutItem(icon = Icons.Default.History, label = "History", onClick = onNavigateToHistory, tag = "history_shortcut")
                    ServiceShortcutItem(icon = Icons.Default.CardGiftcard, label = "Scratch Cards", onClick = onNavigateToRewards, tag = "rewards_shortcut")
                    ServiceShortcutItem(icon = Icons.Default.LocalOffer, label = "Offers", onClick = onNavigateToRewards, tag = "offers_shortcut")
                    ServiceShortcutItem(icon = Icons.Default.AdminPanelSettings, label = "Admin", onClick = onNavigateToAdmin, tag = "admin_shortcut")
                }
            }

            // Promotional Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Flat ₹50 Cashback on Jio 5G", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                            Text("Use code QUICK50 on recharges above ₹299", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        }
                    }
                }
            }

            // Recent Recharges
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Recharges", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onNavigateToHistory) {
                        Text("View All")
                    }
                }
            }

            items(transactions.take(2)) { txn ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${txn.operator} • ${txn.mobileNumber}", fontWeight = FontWeight.Bold)
                            Text(txn.planDetails, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(txn.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("₹${txn.amount}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    txn.status,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceShortcutItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit, tag: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
            .testTag(tag)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}
