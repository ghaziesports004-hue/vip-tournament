package com.example.fsbattle.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fsbattle.data.models.MatchScheduleItem
import com.example.fsbattle.data.models.Tournament
import com.example.fsbattle.data.models.TournamentRegistration
import com.example.fsbattle.ui.theme.*

@Composable
fun MyMatchesScreen(
    tournaments: List<Tournament>,
    userRegistrations: List<TournamentRegistration>,
    matches: List<MatchScheduleItem>
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: My Rooms, 1: Match Schedule
    val context = LocalContext.current

    val registeredTournamentIds = remember(userRegistrations) {
        userRegistrations.map { it.tournamentId }.toSet()
    }
    val myJoinedTournaments = remember(tournaments, registeredTournamentIds) {
        tournaments.filter { registeredTournamentIds.contains(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "MY BATTLES & ROOMS",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Access your verified Room IDs and Passwords for joined tournaments",
            style = MaterialTheme.typography.bodySmall,
            color = Slate600
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Slate800,
            contentColor = ElectricBlue,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ElectricBlue
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "My Rooms (${myJoinedTournaments.size})",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) ElectricBlue else Slate600
                    )
                },
                modifier = Modifier.testTag("tab_my_rooms")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Schedule (${matches.size})",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) ElectricBlue else Slate600
                    )
                },
                modifier = Modifier.testTag("tab_schedule")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            if (myJoinedTournaments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = Slate600,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Joined Tournaments Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate100,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Explore tournaments on the Home tab to join battles!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(myJoinedTournaments) { tournament ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, EmeraldGreen, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Slate800)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tournament.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = EmeraldGreen
                                    ) {
                                        Text(
                                            text = "VERIFIED",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = Slate600, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = tournament.mapName, style = MaterialTheme.typography.bodySmall, color = Slate100)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Slate600, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = tournament.startDate, style = MaterialTheme.typography.bodySmall, color = Slate100)
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Room Credentials Box
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp)),
                                    color = Slate900
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = "ROOM ID", style = MaterialTheme.typography.labelSmall, color = Slate600)
                                                Text(
                                                    text = tournament.roomId ?: "FS-ROOM-8821",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = CyanHighlight,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Room ID", tournament.roomId ?: "FS-ROOM-8821")
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Room ID Copied!", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Room ID", tint = CyanHighlight)
                                            }
                                        }

                                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Slate700)

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
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
                                            IconButton(
                                                onClick = {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Room Password", tournament.roomPassword ?: "7890")
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Room Password Copied!", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Password", tint = GoldenCoin)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Match Schedule List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(matches) { match ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = match.tournamentName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Match #${match.matchNumber} • Map: ${match.mapName} • ${match.scheduledTime}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ElectricBlue
                            ) {
                                Text(
                                    text = match.status.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
