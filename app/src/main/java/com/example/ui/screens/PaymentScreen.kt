package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RechargePlan
import com.example.data.RechargeRepository
import com.example.data.Transaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    operator: String,
    mobileNumber: String,
    planId: String,
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    val plans = RechargeRepository.getPlansForOperator(operator)
    val plan = plans.find { it.id == planId } ?: plans.first()

    var promoCode by remember { mutableStateOf("") }
    var discountAmount by remember { mutableStateOf(0) }
    var promoApplied by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf("Google Pay UPI") }
    var isProcessing by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val finalAmount = (plan.price - discountAmount).coerceAtLeast(0)

    val paymentMethods = listOf(
        "Google Pay UPI", "PhonePe UPI", "Paytm UPI",
        "Credit / Debit Card", "Net Banking", "Quick Recharge Wallet"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm & Pay", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("payment_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Recharge Details", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(operator, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Mobile: $mobileNumber", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            Text("Plan: ₹${plan.price} • ${plan.validity}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(plan.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Base Amount")
                                Text("₹${plan.price}", fontWeight = FontWeight.Bold)
                            }
                            if (promoApplied) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Promo Discount", color = Color(0xFF2E7D32))
                                    Text("-₹$discountAmount", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Platform Fee", color = Color.Gray)
                                Text("FREE", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Payable", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("₹$finalAmount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Apply Promo Code", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = promoCode,
                                    onValueChange = { promoCode = it.uppercase() },
                                    placeholder = { Text("e.g. QUICK50") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("promo_code_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Button(
                                    onClick = {
                                        if (promoCode == "QUICK50" || promoCode == "RECHARGE50") {
                                            discountAmount = 50
                                            promoApplied = true
                                        } else if (promoCode == "FIRST10") {
                                            discountAmount = (plan.price * 0.1).toInt()
                                            promoApplied = true
                                        } else {
                                            discountAmount = 20
                                            promoApplied = true
                                        }
                                    },
                                    modifier = Modifier.testTag("apply_promo_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Apply")
                                }
                            }
                            if (promoApplied) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Promo code applied successfully! Saved ₹$discountAmount", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    Text("Select Payment Method", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        paymentMethods.forEach { method ->
                            val isSelected = selectedPaymentMethod == method
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedPaymentMethod = method }
                                    .testTag("payment_method_${method.take(4).lowercase()}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = when {
                                                method.contains("UPI") -> Icons.Default.QrCode
                                                method.contains("Card") -> Icons.Default.CreditCard
                                                method.contains("Net") -> Icons.Default.AccountBalance
                                                else -> Icons.Default.AccountBalanceWallet
                                            },
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(method, fontWeight = FontWeight.SemiBold)
                                    }
                                    RadioButton(selected = isSelected, onClick = { selectedPaymentMethod = method })
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        isProcessing = true
                        coroutineScope.launch {
                            delay(2000)
                            isProcessing = false
                            val txnId = "TXN" + (10000000..99999999).random()
                            val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
                            RechargeRepository.addTransaction(
                                Transaction(
                                    id = txnId,
                                    mobileNumber = mobileNumber,
                                    operator = operator,
                                    circle = "Maharashtra & Goa",
                                    amount = finalAmount,
                                    planDetails = "${plan.validity} • ${plan.data}",
                                    date = dateStr,
                                    status = "SUCCESS",
                                    paymentMethod = selectedPaymentMethod,
                                    cashbackEarned = 15
                                )
                            )
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("pay_now_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Pay ₹$finalAmount Securely", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (isProcessing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Processing Secure Payment...", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Connecting with $selectedPaymentMethod", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(48.dp)) },
                    title = { Text("Recharge Successful!", fontWeight = FontWeight.Bold) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Your ₹$finalAmount recharge for $mobileNumber ($operator) was successful.", textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                Text("🎉 Scratch Card Unlocked! Check Rewards.", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                onPaymentSuccess()
                            },
                            modifier = Modifier.testTag("done_success_button")
                        ) {
                            Text("View Dashboard")
                        }
                    }
                )
            }
        }
    }
}
