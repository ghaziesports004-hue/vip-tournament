package com.example.fsbattle.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdUnits
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.fsbattle.data.models.EarnCoinsState
import com.example.fsbattle.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun EarnCoinsScreen(
    earnCoinsState: EarnCoinsState,
    onWatchAd: () -> Unit
) {
    var isWatchingAd by remember { mutableStateOf(false) }
    var adProgress by remember { mutableFloatStateOf(0f) }
    var remainingCooldownMs by remember { mutableLongStateOf(0L) }

    // Live Countdown Timer (HH:MM:SS format)
    LaunchedEffect(earnCoinsState.lastAdTimestamp, earnCoinsState.cooldownDurationMs) {
        while (true) {
            val now = System.currentTimeMillis()
            val nextAvailableTime = earnCoinsState.lastAdTimestamp + earnCoinsState.cooldownDurationMs
            val diff = nextAvailableTime - now
            remainingCooldownMs = if (diff > 0) diff else 0L
            delay(1000)
        }
    }

    // Format HH:MM:SS
    val formattedCountdown = remember(remainingCooldownMs) {
        val seconds = (remainingCooldownMs / 1000) % 60
        val minutes = (remainingCooldownMs / (1000 * 60)) % 60
        val hours = (remainingCooldownMs / (1000 * 60 * 60))
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    }

    val isLimitReached = earnCoinsState.adsWatchedToday >= earnCoinsState.maxAdsPerDay
    val isOnCooldown = remainingCooldownMs > 0L && earnCoinsState.adsWatchedToday > 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, GoldenCoin, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = GoldenCoin.copy(alpha = 0.2f),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = GoldenCoin,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Unity Ads Test Mode Badge
                Surface(
                    color = ElectricBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = CyanHighlight, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "UNITY ADS TEST MODE ENABLED",
                            color = CyanHighlight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "EARN FREE COINS",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Unity Ads Rewarded Video (Placement: ${earnCoinsState.placementId})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rewards & Limits Info Grid
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    color = Slate900
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Reward Per Ad:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text("+${earnCoinsState.coinsPerAd} Coins", color = GoldenCoin, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Daily Limit:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text("${earnCoinsState.adsWatchedToday}/${earnCoinsState.maxAdsPerDay} Completed", color = ElectricBlue, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = Slate700)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Real Unity Ads Placement Details
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Unity Game ID:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text(earnCoinsState.gameId, style = MaterialTheme.typography.bodySmall, color = CyanHighlight, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Rewarded Placement:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text(earnCoinsState.placementId, style = MaterialTheme.typography.bodySmall, color = GoldenCoin, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Interstitial Placement:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text(earnCoinsState.interstitialPlacementId, style = MaterialTheme.typography.bodySmall, color = Slate100)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Banner Placement:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                Text(earnCoinsState.bannerPlacementId, style = MaterialTheme.typography.bodySmall, color = Slate100)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (!earnCoinsState.isAdsEnabled) {
                    // Feature Disabled by Admin
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonRed)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LockClock, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("ADS FEATURE DISABLED BY ADMIN", color = CrimsonRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "The admin has temporarily paused Unity Ads rewards. Please try again later.",
                                color = Slate100,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (!earnCoinsState.isAdAvailable) {
                    // Ads Not Available / No Fill State
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.15f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LockClock, contentDescription = null, tint = CrimsonRed)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("ADS NOT AVAILABLE RIGHT NOW", color = CrimsonRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                    Text(
                                        "No video ad filled by Unity server. You cannot watch or earn coins until an ad becomes available.",
                                        color = Slate100,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("watch_ad_btn_disabled"),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Slate700,
                                disabledContentColor = Slate600
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "NO ADS AVAILABLE - CANNOT EARN COINS",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                } else if (isWatchingAd) {
                    // Ad Video Simulation Progress
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Streaming Rewarded Video Ad...", color = CyanHighlight, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { adProgress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = GoldenCoin,
                            trackColor = Slate700
                        )
                    }

                    LaunchedEffect(Unit) {
                        for (i in 1..10) {
                            delay(300)
                            adProgress = i / 10f
                        }
                        onWatchAd()
                        isWatchingAd = false
                        adProgress = 0f
                    }
                } else if (isLimitReached) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LockClock, contentDescription = null, tint = CrimsonRed)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Daily limit of ${earnCoinsState.maxAdsPerDay} ads reached! Please check back tomorrow.",
                                color = CrimsonRed,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (isOnCooldown) {
                    // Active Cooldown Timer with HH:MM:SS Countdown
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Slate900),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldenCoin)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LockClock, contentDescription = null, tint = GoldenCoin)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("REWARD COOLDOWN ACTIVE", color = GoldenCoin, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formattedCountdown,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "Next ad unlocks after cooldown expires",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            if (earnCoinsState.isAdsEnabled && earnCoinsState.isAdAvailable) {
                                isWatchingAd = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("watch_ad_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.PlayCircleFilled, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "WATCH REWARDED AD (+${earnCoinsState.coinsPerAd} COINS)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }

    // Full Screen Unity Rewarded Video Ad Overlay Dialog
    if (isWatchingAd) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { /* Prevent premature dismiss during video ad */ },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            var videoSecondsLeft by remember { mutableIntStateOf(10) }
            var isVideoFinished by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                for (sec in 10 downTo 1) {
                    videoSecondsLeft = sec
                    adProgress = (10 - sec) / 10f
                    delay(1000)
                }
                adProgress = 1f
                isVideoFinished = true
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Video Banner Mock Player Background
                Image(
                    painter = painterResource(id = R.drawable.img_fs_battle_banner_1785321322705),
                    contentDescription = "Unity Video Ad",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark Gradient Mask
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                // Top Header Overlay (Unity Ads SDK Info)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldenCoin)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = GoldenCoin, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("UNITY REWARDED AD | ID: ${earnCoinsState.gameId}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        color = if (isVideoFinished) GoldenCoin else Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = if (isVideoFinished) "REWARD READY" else "Reward in ${videoSecondsLeft}s",
                            color = if (isVideoFinished) Color.Black else CyanHighlight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                // Center Watermark & Video Play Indicator
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = ElectricBlue.copy(alpha = 0.3f),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = null,
                                tint = GoldenCoin,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "FS CLASH SPONSORED MATCH REWARD",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Placement: ${earnCoinsState.placementId}",
                        color = Slate600,
                        fontSize = 12.sp
                    )
                }

                // Bottom Video Controls & Reward Claim Bar
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Video Progress Line
                    LinearProgressIndicator(
                        progress = { adProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GoldenCoin,
                        trackColor = Slate800
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isVideoFinished) {
                        Button(
                            onClick = {
                                onWatchAd()
                                isWatchingAd = false
                                adProgress = 0f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("claim_ad_reward_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "CLAIM +${earnCoinsState.coinsPerAd} COINS REWARD NOW",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        Button(
                            onClick = { },
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Slate800,
                                disabledContentColor = Slate600
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "PLEASE WATCH FULL VIDEO (${videoSecondsLeft}s REMAINING)",
                                color = Slate600,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
