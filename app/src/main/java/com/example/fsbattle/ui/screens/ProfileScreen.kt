package com.example.fsbattle.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.fsbattle.data.models.UserProfile
import com.example.fsbattle.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onUpdateGamerTag: (gamerTag: String, gameId: String, gameUid: String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var newTagInput by remember { mutableStateOf(userProfile.gamerTag) }
    var newGameIdInput by remember { mutableStateOf(userProfile.gameId) }
    var newGameUidInput by remember { mutableStateOf(userProfile.gameUid) }

    var showTermsModal by remember { mutableStateOf(false) }
    var showPrivacyModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "PLAYER PROFILE",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Manage esports credentials, game IDs, and support",
            style = MaterialTheme.typography.bodySmall,
            color = Slate600
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Gamer Card
        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_1785321304971),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, ElectricBlue, CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userProfile.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Gamer Tag: ${userProfile.gamerTag}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyanHighlight,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("edit_gamer_tag_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Tag", tint = CyanHighlight, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Grid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate900)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Wallet Balance", style = MaterialTheme.typography.labelSmall, color = Slate600)
                        Text(
                            text = "${userProfile.walletBalance} C",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldenCoin,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Divider(modifier = Modifier.height(30.dp).width(1.dp), color = Slate700)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Prize Won", style = MaterialTheme.typography.labelSmall, color = Slate600)
                        Text(
                            text = "+${userProfile.totalWon} C",
                            style = MaterialTheme.typography.titleMedium,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Account Details Section
        Text(
            text = "ESPORTS ACCOUNT DETAILS",
            style = MaterialTheme.typography.labelLarge,
            color = Slate600,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.SportsEsports, contentDescription = null, tint = CyanHighlight, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Game ID (IGN)", style = MaterialTheme.typography.bodyMedium, color = Slate100)
                    }
                    Text(text = userProfile.gameId, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate700)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = CyanHighlight, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Game UID", style = MaterialTheme.typography.bodyMedium, color = Slate100)
                    }
                    Text(text = userProfile.gameUid, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate700)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Slate600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Email Address", style = MaterialTheme.typography.bodyMedium, color = Slate100)
                    }
                    Text(text = userProfile.email, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Support & Platform Settings
        Text(
            text = "SUPPORT & LEGAL",
            style = MaterialTheme.typography.labelLarge,
            color = Slate600,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
            Column {
                // WhatsApp Support Contact
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923001234567"))
                                context.startActivity(intent)
                            } catch (e: Exception) { }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("WhatsApp Support Contact", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate600)
                }

                HorizontalDivider(color = Slate700)

                // Terms & Conditions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTermsModal = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = ElectricBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Terms & Community Rules", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate600)
                }

                HorizontalDivider(color = Slate700)

                // Privacy Policy
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPrivacyModal = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = GoldenCoin)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Privacy Policy", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate600)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Share App Button
        OutlinedButton(
            onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "🎮 Join FS CLASH - Pakistan's #1 Esports Battle Platform!\nPlay Free Fire & PUBG Mobile tournaments, win coin rewards & withdraw cash instantly.\nDownload App: https://fsclash.com/download"
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share FS CLASH App")
                context.startActivity(shareIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("share_app_btn"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldenCoin),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldenCoin),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = GoldenCoin)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SHARE FS CLASH APP WITH FRIENDS", color = GoldenCoin, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOG OUT OF ACCOUNT", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Version Badge
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "FS CLASH App Version v1.0.0 (Build 100)",
                style = MaterialTheme.typography.bodySmall,
                color = Slate600
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(100.dp))
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = Slate800,
            title = {
                Text(text = "Edit Gamer Credentials", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTagInput,
                        onValueChange = { newTagInput = it },
                        label = { Text("Gamer Tag") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newGameIdInput,
                        onValueChange = { newGameIdInput = it },
                        label = { Text("In-Game Name (Game ID)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newGameUidInput,
                        onValueChange = { newGameUidInput = it },
                        label = { Text("Numeric Player UID") },
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
                        onUpdateGamerTag(newTagInput, newGameIdInput, newGameUidInput)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text(text = "SAVE", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text(text = "Cancel", color = Slate600)
                }
            }
        )
    }

    // Terms Modal
    if (showTermsModal) {
        AlertDialog(
            onDismissRequest = { showTermsModal = false },
            containerColor = Slate800,
            title = { Text("Terms & Rules", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "1. All players must present authentic Game IDs.\n" +
                    "2. Hacking or team teaming results in permanent device ban.\n" +
                    "3. Room credentials are top secret and non-shareable.",
                    color = Slate100
                )
            },
            confirmButton = {
                Button(onClick = { showTermsModal = false }, colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)) {
                    Text("CLOSE")
                }
            }
        )
    }

    // Privacy Modal
    if (showPrivacyModal) {
        AlertDialog(
            onDismissRequest = { showPrivacyModal = false },
            containerColor = Slate800,
            title = { Text("Privacy Policy", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "FS CLASH values user data privacy. All tournament data and transactions are encrypted securely.",
                    color = Slate100
                )
            },
            confirmButton = {
                Button(onClick = { showPrivacyModal = false }, colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)) {
                    Text("CLOSE")
                }
            }
        )
    }
}
