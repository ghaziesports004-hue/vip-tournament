package com.example.fsbattle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.fsbattle.ui.theme.*

@Composable
fun AuthScreen(
    isDeviceBanned: Boolean = false,
    onLoginSuccess: (email: String, gamerTag: String) -> Unit,
    onRegisterSuccess: (email: String, gamerTag: String, password: String) -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("player1@fsbattle.com") }
    var gamerTagInput by remember { mutableStateOf("FS_SHADOW_FF") }
    var passwordInput by remember { mutableStateOf("password123") }
    var confirmPasswordInput by remember { mutableStateOf("password123") }
    
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmailInput by remember { mutableStateOf("") }
    var forgotMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, ElectricBlue, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_1785321304971),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(2.dp, ElectricBlue, CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "FS CLASH",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (isSignUp) "Create User Account" else "Esports Player Login",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Banned Device Warning
                if (isDeviceBanned && isSignUp) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Block, contentDescription = null, tint = CrimsonRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "This device has been banned from registering new accounts.",
                                color = CrimsonRed,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = CrimsonRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (isSignUp) {
                    // Registration Fields: Email, Username, Password, Confirm Password
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Enter Email Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_reg_email"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = gamerTagInput,
                        onValueChange = { gamerTagInput = it },
                        label = { Text("Enter Username (Gamer Tag)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_reg_username"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Enter Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_reg_password"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPasswordInput,
                        onValueChange = { confirmPasswordInput = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_reg_confirm_password"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                } else {
                    // Login Fields: Email Address, Password
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Enter Email Address") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_login_email"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Enter Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_login_password"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            forgotEmailInput = emailInput
                            showForgotPasswordDialog = true
                        }) {
                            Text("Forgot Password?", color = CyanHighlight, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        errorMessage = null
                        if (isSignUp) {
                            if (isDeviceBanned) {
                                errorMessage = "This device has been banned from registering new accounts."
                                return@Button
                            }
                            if (emailInput.isBlank() || gamerTagInput.isBlank() || passwordInput.isBlank()) {
                                errorMessage = "Please fill in all required fields."
                                return@Button
                            }
                            if (passwordInput != confirmPasswordInput) {
                                errorMessage = "Passwords do not match."
                                return@Button
                            }
                            onRegisterSuccess(emailInput, gamerTagInput, passwordInput)
                        } else {
                            if (emailInput.isBlank() || passwordInput.isBlank()) {
                                errorMessage = "Please enter email and password."
                                return@Button
                            }
                            onLoginSuccess(emailInput, gamerTagInput)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isSignUp) "CREATE ACCOUNT" else "LOGIN",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        isSignUp = !isSignUp
                        errorMessage = null
                    }
                ) {
                    Text(
                        text = if (isSignUp) "Already have an account? Login" else "New to FS Clash? Create Account",
                        color = CyanHighlight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            containerColor = Slate800,
            title = {
                Text("Reset Password", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Enter your email address to receive password reset instructions:", color = Slate100, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = forgotEmailInput,
                        onValueChange = { forgotEmailInput = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    if (forgotMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(forgotMessage!!, color = EmeraldGreen, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (forgotEmailInput.isNotBlank()) {
                            forgotMessage = "Password reset instructions sent to $forgotEmailInput!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("SEND RESET LINK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("CANCEL", color = Slate600)
                }
            }
        )
    }
}
