package com.example.fsbattle.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.fsbattle.data.models.Tournament
import com.example.fsbattle.data.models.UserProfile
import com.example.fsbattle.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailModal(
    tournament: Tournament,
    userProfile: UserProfile,
    isRegistered: Boolean,
    onDismiss: () -> Unit,
    onRegister: (teamName: String, teamMembers: List<String>) -> Unit,
    onNavigateToWallet: () -> Unit
) {
    val context = LocalContext.current
    var gameIdInput by remember { mutableStateOf(userProfile.gameId) }
    var gameUidInput by remember { mutableStateOf(userProfile.gameUid) }
    var teamNameInput by remember { mutableStateOf("FS_ALPHA_SQUAD") }
    var iglGameIdInput by remember { mutableStateOf(userProfile.gameId) }
    var iglUidInput by remember { mutableStateOf(userProfile.gameUid) }

    val isSolo = tournament.format.equals("solo", ignoreCase = true)
    val isDuo = tournament.format.equals("duo", ignoreCase = true)
    val isSquad = tournament.format.equals("squad", ignoreCase = true)
    val hasEnoughBalance = userProfile.walletBalance >= tournament.entryFee

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tournament.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${tournament.category} • ${tournament.format} • ${tournament.mapName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyanHighlight
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_detail_modal")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Slate100)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Banner Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_fs_battle_banner_1785321322705),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Match Specs Grid
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                color = Slate900
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Time:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text(tournament.startDate, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Mode:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text(tournament.format, color = ElectricBlue, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Min Slots:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text("${tournament.minSlotRequired} Players", color = GoldenCoin, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate700)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Entry Fee:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text("${tournament.entryFee} Coins", color = EmeraldGreen, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Kill Reward:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text("+${tournament.killReward} C/Kill", color = CyanHighlight, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Joined:", color = Slate600, style = MaterialTheme.typography.bodySmall)
                            Text("${tournament.currentParticipants}/${tournament.maxParticipants}", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Live Watch Link Button (Optional)
            tournament.liveWatchUrl?.let { liveUrl ->
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) { }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.OndemandVideo, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WATCH LIVE MATCH STREAM", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- VERIFIED ROOM CREDENTIALS CARD ---
            if (isRegistered) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, EmeraldGreen, RoundedCornerShape(16.dp)),
                    color = EmeraldGreen.copy(alpha = 0.15f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "VERIFIED ROOM CREDENTIALS",
                                style = MaterialTheme.typography.titleMedium,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Room ID Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Slate900)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "ROOM ID", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                Text(
                                    text = tournament.roomId ?: "FS-ROOM-8821",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Room ID", tournament.roomId ?: "FS-ROOM-8821")
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Room ID Copied!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Room Password Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Slate900)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "ROOM PASSWORD", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                Text(
                                    text = tournament.roomPassword ?: "7890",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldenCoin,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Room Password", tournament.roomPassword ?: "7890")
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Room Password Copied!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy")
                            }
                        }
                    }
                }
            } else {
                // Locked Credentials Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Slate700, RoundedCornerShape(16.dp)),
                    color = Slate900
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = GoldenCoin,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Room Credentials Locked",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Room ID and Password are visible ONLY to players who successfully join this match.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Survival Ranking Rewards Section
            Text(
                text = "SURVIVAL RANKING REWARDS",
                style = MaterialTheme.typography.labelLarge,
                color = GoldenCoin,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = Slate900
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    tournament.survivalRankingRewards.forEach { rankText ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(rankText, color = Slate100, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldenCoin, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Match Rules
            Text(
                text = "MATCH RULES",
                style = MaterialTheme.typography.labelLarge,
                color = CyanHighlight,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = Slate900
            ) {
                Text(
                    text = tournament.rules,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate100,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registration Form (if not registered)
            if (!isRegistered) {
                Text(
                    text = "MATCH REGISTRATION FORMS (${tournament.format.uppercase()})",
                    style = MaterialTheme.typography.labelLarge,
                    color = ElectricBlue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isSolo) {
                    // Solo Inputs: Game ID, UID
                    OutlinedTextField(
                        value = gameIdInput,
                        onValueChange = { gameIdInput = it },
                        label = { Text("Game ID (In-Game Name)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_game_id"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = gameUidInput,
                        onValueChange = { gameUidInput = it },
                        label = { Text("Game UID (Numeric Player ID)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_game_uid"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                } else {
                    // Duo / Squad Inputs: Team Name, IGL Game ID, IGL UID
                    OutlinedTextField(
                        value = teamNameInput,
                        onValueChange = { teamNameInput = it },
                        label = { Text("Team Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_team_name"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = iglGameIdInput,
                        onValueChange = { iglGameIdInput = it },
                        label = { Text("IGL Game ID (Team Leader IGN)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_igl_game_id"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = iglUidInput,
                        onValueChange = { iglUidInput = it },
                        label = { Text("IGL UID (Numeric Player ID)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_igl_uid"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Slate700,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Balance summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Wallet Balance:", color = Slate600, style = MaterialTheme.typography.bodyMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = GoldenCoin, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.walletBalance} Coins",
                            color = if (hasEnoughBalance) EmeraldGreen else CrimsonRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (hasEnoughBalance) {
                    Button(
                        onClick = {
                            val name = if (isSolo) "SOLO" else teamNameInput
                            onRegister(name, listOf(iglGameIdInput))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_register_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "JOIN MATCH (${tournament.entryFee} COINS)",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    Button(
                        onClick = onNavigateToWallet,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("deposit_coins_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "INSUFFICIENT BALANCE — DEPOSIT COINS",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                    }
                }
            } else {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "ALREADY JOINED MATCH", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
