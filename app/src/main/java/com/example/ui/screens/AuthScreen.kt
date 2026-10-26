package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    var mobileNumber by remember { mutableStateOf("") }
    var isPhoneLogin by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(96.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "App Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quick Recharge India",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Fastest Mobile Recharges & Guaranteed Rewards for Jio, Airtel, Vi & BSNL",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sign in to access recharges & cashback rewards",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isPhoneLogin) {
                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10) mobileNumber = it },
                            label = { Text("Enter Mobile Number") },
                            leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("phone_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (mobileNumber.length == 10) {
                                    Toast.makeText(context, "OTP Sent to $mobileNumber (Demo OTP: 1234)", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                } else {
                                    Toast.makeText(context, "Please enter valid 10 digit number", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("verify_otp_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Verify & Proceed", fontSize = 16.sp)
                        }
                        TextButton(
                            onClick = { isPhoneLogin = false },
                            modifier = Modifier.testTag("other_options_button")
                        ) {
                            Text("Other Sign-in options")
                        }
                    } else {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Signed in successfully with Google", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("google_signin_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continue with Google", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { isPhoneLogin = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("phone_login_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Login with Mobile Number", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { onLoginSuccess() },
                            modifier = Modifier.testTag("guest_login_button")
                        ) {
                            Text("Skip & Continue as Guest", color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "100% Secure & RBI Compliant Payments",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
