package com.example.fsbattle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.fsbattle.ui.theme.*

@Composable
fun DepositDialog(
    onDismiss: () -> Unit,
    onSubmitDeposit: (amount: Long, gateway: String, trxId: String) -> Unit
) {
    var selectedGateway by remember { mutableStateOf("Easypaisa") }
    var selectedAmount by remember { mutableStateOf(500L) }
    var customAmountText by remember { mutableStateOf("500") }
    var accountHolderInput by remember { mutableStateOf("Hassan Gaming") }
    var trxId by remember { mutableStateOf("") }

    val gateways = listOf("Easypaisa", "JazzCash", "UPaisa", "Other")
    val presetAmounts = listOf(100L, 200L, 500L, 1000L, 2000L)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AddCard,
                    contentDescription = null,
                    tint = GoldenCoin,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Deposit Coins",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Payment Gateway",
                    style = MaterialTheme.typography.labelMedium,
                    color = Slate600
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(gateways) { gateway ->
                        val isSelected = selectedGateway == gateway
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricBlue else Slate700,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedGateway = gateway }
                                .testTag("gateway_$gateway"),
                            color = if (isSelected) ElectricBlue.copy(alpha = 0.2f) else Slate900
                        ) {
                            Text(
                                text = gateway,
                                color = if (isSelected) ElectricBlue else Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Deposit Instructions & Admin Account
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    color = Slate900
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Deposit Instructions ($selectedGateway):",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate600
                        )
                        Text(
                            text = "Send Money to: 0300-1234567",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldenCoin,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Account Title: FS CLASH Official",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate100
                        )
                        Text(
                            text = "Limits: Min 50 Coins • Max 50,000 Coins / day",
                            style = MaterialTheme.typography.bodySmall,
                            color = EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountHolderInput,
                    onValueChange = { accountHolderInput = it },
                    label = { Text("Account Holder Name") },
                    modifier = Modifier.fillMaxWidth().testTag("input_dep_holder"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { customAmountText = it },
                    label = { Text("Deposit Coins Amount") },
                    modifier = Modifier.fillMaxWidth().testTag("input_dep_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = trxId,
                    onValueChange = { trxId = it },
                    label = { Text("Transaction ID (Trx ID / Proof)") },
                    placeholder = { Text("e.g. 98712345601") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_trx_id"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalAmt = customAmountText.toLongOrNull() ?: selectedAmount
                    onSubmitDeposit(finalAmt, selectedGateway, trxId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                modifier = Modifier.testTag("submit_deposit_btn")
            ) {
                Text(text = "SUBMIT DEPOSIT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = Slate600)
            }
        }
    )
}

@Composable
fun WithdrawalDialog(
    userBalance: Long,
    onDismiss: () -> Unit,
    onSubmitWithdrawal: (amount: Long, gateway: String, accountNumber: String, accountTitle: String) -> Unit
) {
    var selectedGateway by remember { mutableStateOf("Easypaisa") }
    var amountText by remember { mutableStateOf("200") }
    var accountNumber by remember { mutableStateOf("03001234567") }
    var accountTitle by remember { mutableStateOf("Hassan Gaming") }

    val gateways = listOf("Easypaisa", "JazzCash", "UPaisa", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Withdraw Coins",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Account Type",
                    style = MaterialTheme.typography.labelMedium,
                    color = Slate600
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(gateways) { gateway ->
                        val isSelected = selectedGateway == gateway
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricBlue else Slate700,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedGateway = gateway },
                            color = if (isSelected) ElectricBlue.copy(alpha = 0.2f) else Slate900
                        ) {
                            Text(
                                text = gateway,
                                color = if (isSelected) ElectricBlue else Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountTitle,
                    onValueChange = { accountTitle = it },
                    label = { Text("Account Holder Name") },
                    placeholder = { Text("Hassan Gaming") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_wd_title"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("$selectedGateway Account Number") },
                    placeholder = { Text("03001234567") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_wd_account"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Withdraw Coin Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_wd_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toLongOrNull() ?: 0L
                    onSubmitWithdrawal(amt, selectedGateway, accountNumber, accountTitle)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                modifier = Modifier.testTag("submit_withdrawal_btn")
            ) {
                Text(text = "SUBMIT WITHDRAWAL", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = Slate600)
            }
        }
    )
}

@Composable
fun CoinConversionDialog(
    winningCoins: Long,
    onDismiss: () -> Unit,
    onConvertCoins: (winningAmount: Long) -> Unit
) {
    var convertInputText by remember { mutableStateOf("1000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = CyanHighlight,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Coin Conversion",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    color = Slate900
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Conversion Rule:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate600
                        )
                        Text(
                            text = "1000 Winning Coins = 100 Deposit Coins",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyanHighlight,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Available Winning Coins: $winningCoins C",
                            style = MaterialTheme.typography.bodySmall,
                            color = EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = convertInputText,
                    onValueChange = { convertInputText = it },
                    label = { Text("Winning Coins to Convert") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Slate700,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = convertInputText.toLongOrNull() ?: 0L
                    onConvertCoins(amount)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanHighlight)
            ) {
                Text("CONVERT COINS", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate600)
            }
        }
    )
}
