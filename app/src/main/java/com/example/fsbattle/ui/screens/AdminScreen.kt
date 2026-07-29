package com.example.fsbattle.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fsbattle.data.models.AppUpdateConfig
import com.example.fsbattle.data.models.DepositRequest
import com.example.fsbattle.data.models.EarnCoinsState
import com.example.fsbattle.data.models.Tournament
import com.example.fsbattle.data.models.WithdrawalRequest
import com.example.fsbattle.ui.theme.*

@Composable
fun AdminScreen(
    tournaments: List<Tournament>,
    deposits: List<DepositRequest>,
    withdrawals: List<WithdrawalRequest>,
    earnCoinsState: EarnCoinsState,
    appUpdateConfig: AppUpdateConfig,
    onToggleAdsEnabled: (Boolean) -> Unit,
    onToggleAdAvailability: (Boolean) -> Unit,
    onUpdateAdSettings: (String, Long, Int, Long) -> Unit,
    onPublishAppUpdate: (latestVersion: String, updateUrl: String, updateNotes: String, isMandatory: Boolean) -> Unit,
    onCloseAdmin: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Approvals, 1: Room Credentials, 2: Unity Ads, 3: App Update
    val context = LocalContext.current

    var newRoomId by remember { mutableStateOf("") }
    var newRoomPassword by remember { mutableStateOf("") }

    var gameIdInput by remember { mutableStateOf(earnCoinsState.gameId) }
    var rewardInput by remember { mutableStateOf(earnCoinsState.coinsPerAd.toString()) }
    var maxAdsInput by remember { mutableStateOf(earnCoinsState.maxAdsPerDay.toString()) }

    var latestVersionInput by remember { mutableStateOf(appUpdateConfig.latestAppVersion) }
    var updateUrlInput by remember { mutableStateOf(appUpdateConfig.updateUrl) }
    var updateNotesInput by remember { mutableStateOf(appUpdateConfig.updateNotes) }
    var isMandatoryInput by remember { mutableStateOf(appUpdateConfig.isUpdateMandatory) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FS BATTLE ADMIN CONTROL",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GoldenCoin,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Approve transactions, manage room IDs & Unity Ads controls",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )
            }
            IconButton(
                onClick = onCloseAdmin,
                modifier = Modifier.testTag("close_admin_btn")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Admin", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Overview Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Slate800)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Active Tourneys", style = MaterialTheme.typography.labelSmall, color = Slate600)
                    Text(text = "${tournaments.size}", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Slate800)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Pending Deposits", style = MaterialTheme.typography.labelSmall, color = Slate600)
                    Text(text = "${deposits.count { it.status == "pending" }}", style = MaterialTheme.typography.titleLarge, color = GoldenCoin, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Slate800)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Ads Feature", style = MaterialTheme.typography.labelSmall, color = Slate600)
                    Text(text = if (earnCoinsState.isAdsEnabled) "ACTIVE" else "OFF", style = MaterialTheme.typography.titleLarge, color = if (earnCoinsState.isAdsEnabled) EmeraldGreen else CrimsonRed, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Slate800,
            contentColor = GoldenCoin
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Approvals", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) GoldenCoin else Slate600, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Rooms", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) GoldenCoin else Slate600, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Unity Ads", fontWeight = FontWeight.Bold, color = if (selectedTab == 2) GoldenCoin else Slate600, fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Update Link", fontWeight = FontWeight.Bold, color = if (selectedTab == 3) GoldenCoin else Slate600, fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // Deposit Approvals List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(text = "DEPOSIT REQUESTS", style = MaterialTheme.typography.labelLarge, color = CyanHighlight, fontWeight = FontWeight.Bold)
                }

                items(deposits) { dep ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${dep.gateway} Deposit (${dep.amount} Coins)", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = "Trx ID: ${dep.trxId}", style = MaterialTheme.typography.bodySmall, color = Slate100)
                                Text(text = "Status: ${dep.status.uppercase()}", style = MaterialTheme.typography.labelSmall, color = if (dep.status == "approved") EmeraldGreen else GoldenCoin)
                            }

                            if (dep.status == "pending") {
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Approved Deposit for ${dep.amount} Coins!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                                ) {
                                    Text(text = "APPROVE", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // Room Credentials Management
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tournaments) { tourney ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = tourney.name, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "Current Room ID: ${tourney.roomId ?: "None"} | Pass: ${tourney.roomPassword ?: "None"}", style = MaterialTheme.typography.bodySmall, color = CyanHighlight)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = newRoomId,
                                    onValueChange = { newRoomId = it },
                                    label = { Text("Room ID") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldenCoin,
                                        unfocusedBorderColor = Slate700,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                                OutlinedTextField(
                                    value = newRoomPassword,
                                    onValueChange = { newRoomPassword = it },
                                    label = { Text("Password") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldenCoin,
                                        unfocusedBorderColor = Slate700,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    Toast.makeText(context, "Room Credentials Updated for ${tourney.name}", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "UPDATE ROOM CREDENTIALS", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 2) {
            // Unity Ads Management Panel
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(text = "UNITY ADS MONETIZATION CONTROL", style = MaterialTheme.typography.labelLarge, color = GoldenCoin, fontWeight = FontWeight.Bold)
                }

                // Master Ads Enable/Disable Toggle
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate800),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (earnCoinsState.isAdsEnabled) EmeraldGreen else CrimsonRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Enable Unity Ads Feature",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (earnCoinsState.isAdsEnabled)
                                        "Earn Coins tab is visible in Userpanel navigation."
                                    else
                                        "DISABLE ADS: Earn Coins tab will completely DISAPPEAR from Userpanel!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (earnCoinsState.isAdsEnabled) CyanHighlight else CrimsonRed
                                )
                            }
                            Switch(
                                checked = earnCoinsState.isAdsEnabled,
                                onCheckedChange = { onToggleAdsEnabled(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen, checkedTrackColor = Slate900)
                            )
                        }
                    }
                }

                // Ad Fill Availability Toggle (Simulate No Ads Available)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ad Fill Status (Unity Network)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (earnCoinsState.isAdAvailable)
                                        "Ads Available (Users can watch & earn coins)"
                                    else
                                        "NO ADS AVAILABLE: Users will see 'Ads Not Available' warning & CANNOT earn coins!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (earnCoinsState.isAdAvailable) GoldenCoin else CrimsonRed
                                )
                            }
                            Switch(
                                checked = earnCoinsState.isAdAvailable,
                                onCheckedChange = { onToggleAdAvailability(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = GoldenCoin, checkedTrackColor = Slate900)
                            )
                        }
                    }
                }

                // Unity Ads Configuration Parameters
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Unity Placement Specifications", style = MaterialTheme.typography.titleSmall, color = CyanHighlight, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = gameIdInput,
                                onValueChange = { gameIdInput = it },
                                label = { Text("Unity Game ID") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldenCoin, unfocusedBorderColor = Slate700, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = rewardInput,
                                    onValueChange = { rewardInput = it },
                                    label = { Text("Coins Per Ad") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldenCoin, unfocusedBorderColor = Slate700, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                                OutlinedTextField(
                                    value = maxAdsInput,
                                    onValueChange = { maxAdsInput = it },
                                    label = { Text("Daily Ad Limit") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldenCoin, unfocusedBorderColor = Slate700, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    val reward = rewardInput.toLongOrNull() ?: 50L
                                    val maxAds = maxAdsInput.toIntOrNull() ?: 5
                                    onUpdateAdSettings(gameIdInput, reward, maxAds, 30L)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin)
                            ) {
                                Text("SAVE AD CONFIGURATION", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // App Update & Web Link Remote Config Panel
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(text = "APP VERSION & REMOTE UPDATE CONFIG (FIREBASE SYNC)", style = MaterialTheme.typography.labelLarge, color = GoldenCoin, fontWeight = FontWeight.Bold)
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Installed App Version", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                    Text(text = "v${appUpdateConfig.currentAppVersion}", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Surface(
                                    color = Slate700,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Target: v${appUpdateConfig.latestAppVersion}",
                                        color = GoldenCoin,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = latestVersionInput,
                                onValueChange = { latestVersionInput = it },
                                label = { Text("Latest Published Version (e.g. 1.1.0)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldenCoin,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = updateUrlInput,
                                onValueChange = { updateUrlInput = it },
                                label = { Text("APK Download / Update Web Link") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldenCoin,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = updateNotesInput,
                                onValueChange = { updateNotesInput = it },
                                label = { Text("Release Notes & Announcement") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldenCoin,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Force Mandatory Update",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Switch(
                                    checked = isMandatoryInput,
                                    onCheckedChange = { isMandatoryInput = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = CrimsonRed, checkedTrackColor = Slate900)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    onPublishAppUpdate(
                                        latestVersionInput,
                                        updateUrlInput,
                                        updateNotesInput,
                                        isMandatoryInput
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("PUBLISH UPDATE TO FIREBASE", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
