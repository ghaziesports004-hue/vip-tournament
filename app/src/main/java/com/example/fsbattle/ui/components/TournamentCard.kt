package com.example.fsbattle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.fsbattle.data.models.Tournament
import com.example.fsbattle.ui.theme.*

@Composable
fun TournamentCard(
    tournament: Tournament,
    isRegistered: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatColor = when (tournament.format.lowercase()) {
        "solo" -> ElectricBlue
        "duo" -> CyanHighlight
        "squad" -> GoldenCoin
        else -> ElectricBlue
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isRegistered) EmeraldGreen else Slate700,
                RoundedCornerShape(16.dp)
            )
            .clickable { onCardClick() }
            .testTag("tournament_card_${tournament.id}"),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Header Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_fs_battle_banner_1785321322705),
                    contentDescription = "Tournament Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Slate800),
                                startY = 30f
                            )
                        )
                )

                // Format Badge
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(20.dp),
                    color = formatColor.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (tournament.format.lowercase()) {
                                "solo" -> Icons.Default.Person
                                "duo" -> Icons.Default.People
                                else -> Icons.Default.Groups
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tournament.format.uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Registration Status Badge
                if (isRegistered) {
                    Surface(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(20.dp),
                        color = EmeraldGreen
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "JOINED",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Body Details
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tournament.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Map & Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tournament.mapName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate100
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tournament.startDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate100
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Row: Entry Fee & Prize Pool
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate700.copy(alpha = 0.5f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Entry Fee
                    Column {
                        Text(
                            text = "ENTRY FEE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate600
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldenCoin,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tournament.entryFee} Coins",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldenCoin,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = Slate600
                    )

                    // Prize Pool
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "PRIZE POOL",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate600
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GoldenCoin,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tournament.prizePool} Coins",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Participant Slots Progress
                val progress = if (tournament.maxParticipants > 0) {
                    tournament.currentParticipants.toFloat() / tournament.maxParticipants.toFloat()
                } else 0f

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Slots Filled",
                            style = MaterialTheme.typography.labelSmall,
                            color = Slate600
                        )
                        Text(
                            text = "${tournament.currentParticipants} / ${tournament.maxParticipants}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanHighlight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = CyanHighlight,
                        trackColor = Slate700
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Button
                Button(
                    onClick = onCardClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("tournament_join_btn_${tournament.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRegistered) EmeraldGreen else ElectricBlue
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = if (isRegistered) Icons.Default.VpnKey else Icons.Default.SportsEsports,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRegistered) "VIEW ROOM CREDENTIALS" else "REGISTER NOW (${tournament.entryFee} COINS)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
