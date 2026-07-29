package com.example.fsbattle.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.fsbattle.data.models.HomeSlider
import com.example.fsbattle.data.models.Tournament
import com.example.fsbattle.data.models.TournamentCategory
import com.example.fsbattle.data.models.TournamentRegistration
import com.example.fsbattle.data.models.UserProfile
import com.example.fsbattle.ui.components.BannerAdView
import com.example.fsbattle.ui.components.TournamentCard
import com.example.fsbattle.ui.components.TournamentDetailModal
import com.example.fsbattle.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    tournaments: List<Tournament>,
    categories: List<TournamentCategory>,
    sliders: List<HomeSlider>,
    userRegistrations: List<TournamentRegistration>,
    selectedCategoryFilter: String,
    selectedFormatFilter: String,
    onCategorySelect: (String) -> Unit,
    onFormatSelect: (String) -> Unit,
    onRegister: (Tournament, String, List<String>) -> Unit,
    onNavigateToWallet: () -> Unit
) {
    val context = LocalContext.current
    var selectedTournamentForDetail by remember { mutableStateOf<Tournament?>(null) }
    val registeredIds = remember(userRegistrations) { userRegistrations.map { it.tournamentId }.toSet() }

    // Auto Image Slider State
    var currentSlideIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(sliders) {
        if (sliders.isNotEmpty()) {
            while (true) {
                delay(3500)
                currentSlideIndex = (currentSlideIndex + 1) % sliders.size
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Slate900)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1785321304971),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, ElectricBlue, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = userProfile.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    tint = CyanHighlight,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = userProfile.gamerTag,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CyanHighlight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Wallet Shortcut
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, GoldenCoin, RoundedCornerShape(20.dp))
                            .clickable { onNavigateToWallet() }
                            .testTag("home_wallet_chip"),
                        color = Slate800
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldenCoin,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${userProfile.walletBalance} C",
                                color = GoldenCoin,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            // Auto Image Slider
            item {
                if (sliders.isNotEmpty()) {
                    val activeSlide = sliders[currentSlideIndex % sliders.size]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                activeSlide.webLink?.let { link ->
                                    try {
                                        val intent = Intent(IntentAction.VIEW, Uri.parse(link))
                                        context.startActivity(intent)
                                    } catch (e: Exception) { }
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_fs_battle_banner_1785321322705),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Slate900.copy(alpha = 0.95f))
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Surface(
                                    color = ElectricBlue,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "FEATURED BANNER",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeSlide.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tap to open official link & tournament rules",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate100
                                )
                            }
                        }
                    }
                }
            }

            // Unity Banner Ad Integration
            item {
                BannerAdView(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }

            // Tournament Categories (3-Column Grid with Images)
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GAME CATEGORIES",
                            style = MaterialTheme.typography.labelLarge,
                            color = Slate600,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedCategoryFilter != "All") {
                            TextButton(onClick = { onCategorySelect("All") }) {
                                Text("Show All Games", color = ElectricBlue, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Render in 3-column grid with Image backgrounds
                    val categoryRows = categories.chunked(3)
                    categoryRows.forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { cat ->
                                val isSelected = selectedCategoryFilter.equals(cat.name, ignoreCase = true)
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(
                                            2.dp,
                                            if (isSelected) GoldenCoin else Slate700,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable { onCategorySelect(cat.name) }
                                        .testTag("category_${cat.name}"),
                                    colors = CardDefaults.cardColors(containerColor = Slate800)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        // Category Image Banner Background
                                        val drawableRes = when {
                                            cat.name.contains("Free Fire", true) -> R.drawable.cat_freefire_1785339865300
                                            cat.name.contains("CS", true) || cat.name.contains("Clash", true) -> R.drawable.cat_cs4v4_1785339844328
                                            else -> R.drawable.img_fs_battle_banner_1785321322705
                                        }

                                        Image(
                                            painter = painterResource(id = drawableRes),
                                            contentDescription = cat.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // Dark Overlay Gradient for Readable Text
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            if (isSelected) ElectricBlue.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f),
                                                            if (isSelected) Slate900.copy(alpha = 0.95f) else Slate900.copy(alpha = 0.9f)
                                                        )
                                                    )
                                                )
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = cat.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Surface(
                                                color = if (isSelected) GoldenCoin else Slate900.copy(alpha = 0.8f),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text(
                                                    text = "${cat.tournamentCount} Matches",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = if (isSelected) Color.Black else CyanHighlight,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    fontSize = 9.sp
                                                )
                                            }
                                        }

                                        if (isSelected) {
                                            Surface(
                                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                                                shape = CircleShape,
                                                color = GoldenCoin
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Selected",
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Mode Format Filter Bar (Solo, Duo, Squad)
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCategoryFilter != "All") {
                                "${selectedCategoryFilter.uppercase()} MATCHES"
                            } else {
                                "UPCOMING MATCHES"
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = Slate100,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Format: $selectedFormatFilter",
                            style = MaterialTheme.typography.bodySmall,
                            color = CyanHighlight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("All", "Solo", "Duo", "Squad")) { filter ->
                            val isSelected = selectedFormatFilter.equals(filter, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) ElectricBlue else Slate700,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onFormatSelect(filter) }
                                    .testTag("filter_$filter"),
                                color = if (isSelected) ElectricBlue else Slate800
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (filter) {
                                            "Solo" -> Icons.Default.Person
                                            "Duo" -> Icons.Default.People
                                            "Squad" -> Icons.Default.Groups
                                            else -> Icons.Default.FormatListBulleted
                                        },
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else Slate600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = filter.uppercase(),
                                        color = if (isSelected) Color.White else Slate100,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tournament Cards List
            if (tournaments.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate800)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EventBusy, contentDescription = null, tint = Slate600, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No tournaments available for selected category/filter.", color = Slate100, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            } else {
                items(tournaments) { tournament ->
                    val isJoined = registeredIds.contains(tournament.id)
                    TournamentCard(
                        tournament = tournament,
                        isRegistered = isJoined,
                        onCardClick = { selectedTournamentForDetail = tournament },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Selected Detail Bottom Sheet Modal
        selectedTournamentForDetail?.let { tourney ->
            TournamentDetailModal(
                tournament = tourney,
                userProfile = userProfile,
                isRegistered = registeredIds.contains(tourney.id),
                onDismiss = { selectedTournamentForDetail = null },
                onRegister = { teamName, members ->
                    onRegister(tourney, teamName, members)
                    selectedTournamentForDetail = null
                },
                onNavigateToWallet = {
                    selectedTournamentForDetail = null
                    onNavigateToWallet()
                }
            )
        }
    }
}

private object IntentAction {
    const val VIEW = "android.intent.action.VIEW"
}
