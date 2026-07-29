package com.example.fsbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.fsbattle.data.models.CachedTransaction
import com.example.fsbattle.data.models.UserProfile
import com.example.fsbattle.ui.components.BannerAdView
import com.example.fsbattle.ui.components.CoinConversionDialog
import com.example.fsbattle.ui.components.DepositDialog
import com.example.fsbattle.ui.components.WithdrawalDialog
import com.example.fsbattle.ui.theme.*

@Composable
fun WalletScreen(
    userProfile: UserProfile,
    transactions: List<CachedTransaction>,
    onRequestDeposit: (amount: Long, gateway: String, trxId: String) -> Unit,
    onRequestWithdrawal: (amount: Long, gateway: String, accountNumber: String, accountTitle: String) -> Unit,
    onConvertCoins: (winningAmount: Long) -> Unit
) {
    var showDepositDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var showConvertDialog by remember { mutableStateOf(false) }
    var selectedTxFilter by remember { mutableStateOf("All") }

    val filteredTxs = remember(transactions, selectedTxFilter) {
        if (selectedTxFilter == "All") transactions else {
            transactions.filter {
                when (selectedTxFilter) {
                    "Deposit" -> it.type.contains("deposit", ignoreCase = true)
                    "Withdrawal" -> it.type.contains("withdrawal", ignoreCase = true)
                    "Fee" -> it.type.contains("fee", ignoreCase = true)
                    "Reward" -> it.type.contains("reward", ignoreCase = true) || it.type.contains("ad", ignoreCase = true)
                    else -> true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Slate900)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 100.dp, top = 16.dp)
        ) {
            item {
                Text(
                    text = "DIGITAL WALLET",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Deposit, withdraw & convert your esports coins balance",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Wallet Balances Breakdown Grid Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.5.dp, GoldenCoin, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Slate800)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "TOTAL AVAILABLE COINS",
                            style = MaterialTheme.typography.labelMedium,
                            color = Slate600,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldenCoin,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${userProfile.walletBalance}",
                                style = MaterialTheme.typography.displayLarge,
                                color = GoldenCoin,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Coins",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate100
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(color = Slate700)

                        Spacer(modifier = Modifier.height(12.dp))

                        // Deposit Coins vs Winning Coins Breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Deposit Coins", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                    Text(
                                        text = "${userProfile.depositCoins} C",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = ElectricBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f).padding(start = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Winning Coins", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                    Text(
                                        text = "${userProfile.winningCoins} C",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons Row: DEPOSIT, WITHDRAW, CONVERT
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showDepositDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("wallet_deposit_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "DEPOSIT", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = { showWithdrawDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("wallet_withdraw_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "WITHDRAW", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = { showConvertDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("wallet_convert_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanHighlight),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "CONVERT", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Unity Banner Ad Integration
            item {
                BannerAdView(
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Transaction History Title & Filters
            item {
                Column {
                    Text(
                        text = "WALLET TRANSACTION HISTORY",
                        style = MaterialTheme.typography.labelLarge,
                        color = Slate600,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("All", "Deposit", "Withdrawal", "Fee", "Reward")) { filter ->
                            val isSelected = selectedTxFilter == filter
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) ElectricBlue else Slate700,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { selectedTxFilter = filter },
                                color = if (isSelected) ElectricBlue else Slate800
                            ) {
                                Text(
                                    text = filter,
                                    color = if (isSelected) Color.White else Slate100,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Transaction Items List
            if (filteredTxs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No transactions found", color = Slate600)
                    }
                }
            } else {
                items(filteredTxs) { tx ->
                    val isPositive = tx.type.contains("deposit", true) || tx.type.contains("reward", true) || tx.type.contains("convert", true)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isPositive) EmeraldGreen.copy(0.2f) else CrimsonRed.copy(0.2f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isPositive) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                            contentDescription = null,
                                            tint = if (isPositive) EmeraldGreen else CrimsonRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = tx.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = tx.status.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = when (tx.status.lowercase()) {
                                            "accepted", "approved" -> EmeraldGreen
                                            "rejected" -> CrimsonRed
                                            else -> GoldenCoin
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = (if (isPositive) "+" else "-") + "${tx.amount} C",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isPositive) EmeraldGreen else CrimsonRed,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Bal: ${tx.balanceAfter}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Slate600
                                )
                            }
                        }
                    }
                }
            }
        }

        // Deposit Modal
        if (showDepositDialog) {
            DepositDialog(
                onDismiss = { showDepositDialog = false },
                onSubmitDeposit = { amount, gateway, trxId ->
                    onRequestDeposit(amount, gateway, trxId)
                    showDepositDialog = false
                }
            )
        }

        // Withdrawal Modal
        if (showWithdrawDialog) {
            WithdrawalDialog(
                userBalance = userProfile.walletBalance,
                onDismiss = { showWithdrawDialog = false },
                onSubmitWithdrawal = { amount, gateway, accountNumber, accountTitle ->
                    onRequestWithdrawal(amount, gateway, accountNumber, accountTitle)
                    showWithdrawDialog = false
                }
            )
        }

        // Coin Conversion Modal
        if (showConvertDialog) {
            CoinConversionDialog(
                winningCoins = userProfile.winningCoins,
                onDismiss = { showConvertDialog = false },
                onConvertCoins = { winningAmt ->
                    onConvertCoins(winningAmt)
                    showConvertDialog = false
                }
            )
        }
    }
}
