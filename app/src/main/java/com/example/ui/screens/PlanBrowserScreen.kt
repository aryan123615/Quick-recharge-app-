package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargePlan
import com.example.data.RechargeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanBrowserScreen(
    operator: String,
    mobileNumber: String,
    onBack: () -> Unit,
    onSelectPlan: (RechargePlan) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Popular") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("Popular", "Unlimited", "Data", "Cricket", "Talktime")
    val plans = RechargeRepository.getPlansForOperator(operator)

    val filteredPlans = plans.filter { plan ->
        val matchesCategory = plan.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                plan.price.toString().contains(searchQuery) ||
                plan.description.contains(searchQuery, ignoreCase = true) ||
                plan.data.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("$operator Recharge • $mobileNumber", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Select a prepaid plan", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by price, data, or benefits...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("plan_search_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("tab_${category.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPlans) { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹${plan.price}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (plan.isPopular) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.errorContainer
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocalFireDepartment,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    "POPULAR",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.error,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                                Button(
                                    onClick = { onSelectPlan(plan) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("select_plan_${plan.id}")
                                ) {
                                    Text("Recharge")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                PlanDetailBadge(label = "Validity", value = plan.validity)
                                PlanDetailBadge(label = "Data", value = plan.data)
                                PlanDetailBadge(label = "Calls", value = "Unlimited")
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = plan.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanDetailBadge(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
