package com.example

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fsbattle.ui.FsBattleViewModel
import com.example.fsbattle.ui.screens.*
import com.example.fsbattle.ui.theme.*
import com.example.fsbattle.utils.NotificationHelper

class MainActivity : ComponentActivity() {

    private val viewModel: FsBattleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FsBattleTheme {
                FsBattleMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FsBattleMainApp(viewModel: FsBattleViewModel) {
    val context = LocalContext.current
    var selectedBottomTab by remember { mutableIntStateOf(0) }

    // Request Notification Permission on App Launch (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            NotificationHelper.createNotificationChannel(context)
            NotificationHelper.showSystemNotification(
                context,
                "FS CLASH Notifications Enabled!",
                "You will receive live room credentials, match updates, and instant coin reward alerts."
            )
        }
    }

    LaunchedEffect(Unit) {
        NotificationHelper.createNotificationChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    } // 0: Home, 1: My Battles, 2: Earn Coins, 3: Leaderboard, 4: Wallet, 5: Results, 6: Alerts, 7: Profile

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val isDeviceBanned by viewModel.isDeviceBanned.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val sliders by viewModel.sliders.collectAsStateWithLifecycle()
    val filteredTournaments by viewModel.filteredTournaments.collectAsStateWithLifecycle()
    val selectedFormatFilter by viewModel.selectedFormatFilter.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val userRegistrations by viewModel.userRegistrations.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val deposits by viewModel.deposits.collectAsStateWithLifecycle()
    val withdrawals by viewModel.withdrawals.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val leaderboardEntries by viewModel.leaderboardEntries.collectAsStateWithLifecycle()
    val tournamentResults by viewModel.tournamentResults.collectAsStateWithLifecycle()
    val earnCoinsState by viewModel.earnCoinsState.collectAsStateWithLifecycle()
    val appUpdateConfig by viewModel.appUpdateConfig.collectAsStateWithLifecycle()

    // Redirect tab if Earn tab is disabled by admin
    LaunchedEffect(earnCoinsState.isAdsEnabled) {
        if (!earnCoinsState.isAdsEnabled && selectedBottomTab == 2) {
            selectedBottomTab = 0
        }
    }

    // Listen to UI messages for Toast feedback
    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    // 1. Account Banned / Suspended Screen Check
    if (currentUser.isSuspended) {
        AccountSuspendedScreen(
            banReason = currentUser.banReason,
            deviceId = "DEV_ANDROID_8820",
            onLogout = { viewModel.logoutUser() }
        )
        return
    }

    // 2. Authentication Screen Check
    if (!isLoggedIn) {
        AuthScreen(
            isDeviceBanned = isDeviceBanned,
            onLoginSuccess = { email, tag -> viewModel.loginUser(email, tag) },
            onRegisterSuccess = { email, tag, pass -> viewModel.registerUser(email, tag, pass) }
        )
        return
    }

    // 3. Main Userpanel Interface with Navigation
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Slate900,
        bottomBar = {
            NavigationBar(
                containerColor = Slate800,
                contentColor = ElectricBlue,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = selectedBottomTab == 0,
                    onClick = { selectedBottomTab = 0 },
                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Home") },
                    label = { Text("Home", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_home"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricBlue,
                        selectedTextColor = ElectricBlue,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 1,
                    onClick = { selectedBottomTab = 1 },
                    icon = { Icon(Icons.Default.VpnKey, contentDescription = "Battles") },
                    label = { Text("Battles", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_my_battles"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen,
                        selectedTextColor = EmeraldGreen,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )

                // Earn Coins Tab (Completely hidden from userpanel when disabled by Admin)
                if (earnCoinsState.isAdsEnabled) {
                    NavigationBarItem(
                        selected = selectedBottomTab == 2,
                        onClick = { selectedBottomTab = 2 },
                        icon = { Icon(Icons.Default.MonetizationOn, contentDescription = "Earn") },
                        label = { Text("Earn", fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_earn"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldenCoin,
                            selectedTextColor = GoldenCoin,
                            unselectedIconColor = Slate600,
                            unselectedTextColor = Slate600,
                            indicatorColor = Slate700
                        )
                    )
                }
                NavigationBarItem(
                    selected = selectedBottomTab == 3,
                    onClick = { selectedBottomTab = 3 },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Ranks") },
                    label = { Text("Ranks", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_ranks"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldenCoin,
                        selectedTextColor = GoldenCoin,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 4,
                    onClick = { selectedBottomTab = 4 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
                    label = { Text("Wallet", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_wallet"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GoldenCoin,
                        selectedTextColor = GoldenCoin,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 5,
                    onClick = { selectedBottomTab = 5 },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Results") },
                    label = { Text("Results", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_results"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyanHighlight,
                        selectedTextColor = CyanHighlight,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 6,
                    onClick = { selectedBottomTab = 6 },
                    icon = {
                        BadgedBox(
                            badge = {
                                val unreadCount = notifications.count { !it.read }
                                if (unreadCount > 0) {
                                    Badge(containerColor = CrimsonRed) {
                                        Text(text = "$unreadCount", color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts")
                        }
                    },
                    label = { Text("Alerts", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_notifications"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyanHighlight,
                        selectedTextColor = CyanHighlight,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
                NavigationBarItem(
                    selected = selectedBottomTab == 7,
                    onClick = { selectedBottomTab = 7 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_profile"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricBlue,
                        selectedTextColor = ElectricBlue,
                        unselectedIconColor = Slate600,
                        unselectedTextColor = Slate600,
                        indicatorColor = Slate700
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedBottomTab) {
                0 -> HomeScreen(
                    userProfile = currentUser,
                    tournaments = filteredTournaments,
                    categories = categories,
                    sliders = sliders,
                    userRegistrations = userRegistrations,
                    selectedCategoryFilter = selectedCategoryFilter,
                    selectedFormatFilter = selectedFormatFilter,
                    onCategorySelect = { viewModel.setCategoryFilter(it) },
                    onFormatSelect = { viewModel.setFormatFilter(it) },
                    onRegister = { tourney, teamName, members ->
                        viewModel.registerForTournament(tourney, teamName, members)
                    },
                    onNavigateToWallet = { selectedBottomTab = 4 }
                )
                1 -> MyMatchesScreen(
                    tournaments = tournaments,
                    userRegistrations = userRegistrations,
                    matches = matches
                )
                2 -> EarnCoinsScreen(
                    earnCoinsState = earnCoinsState,
                    onWatchAd = { viewModel.watchAdReward() }
                )
                3 -> LeaderboardScreen(
                    leaderboardEntries = leaderboardEntries
                )
                4 -> WalletScreen(
                    userProfile = currentUser,
                    transactions = transactions,
                    onRequestDeposit = { amount, gateway, trxId ->
                        viewModel.requestDeposit(amount, gateway, trxId)
                    },
                    onRequestWithdrawal = { amount, gateway, accNum, accTitle ->
                        viewModel.requestWithdrawal(amount, gateway, accNum, accTitle)
                    },
                    onConvertCoins = { winningAmt ->
                        viewModel.convertCoins(winningAmt)
                    }
                )
                5 -> ResultsScreen(
                    results = tournamentResults
                )
                6 -> NotificationsScreen(
                    notifications = notifications,
                    onMarkRead = { viewModel.markNotificationRead(it) },
                    onClearAll = { viewModel.clearNotifications() }
                )
                7 -> ProfileScreen(
                    userProfile = currentUser,
                    onUpdateGamerTag = { tag, gameId, gameUid ->
                        viewModel.updateGamerTag(tag, gameId, gameUid)
                    },
                    onLogout = { viewModel.logoutUser() }
                )
            }
        }
    }

    // App Remote Update Available Dialog (Firebase Powered)
    var isUpdateDialogDismissed by remember { mutableStateOf(false) }
    if (appUpdateConfig.latestAppVersion != appUpdateConfig.currentAppVersion && !isUpdateDialogDismissed) {
        AlertDialog(
            onDismissRequest = {
                if (!appUpdateConfig.isUpdateMandatory) {
                    isUpdateDialogDismissed = true
                }
            },
            containerColor = Slate800,
            icon = {
                Icon(Icons.Default.CloudDownload, contentDescription = null, tint = GoldenCoin, modifier = Modifier.size(36.dp))
            },
            title = {
                Text(
                    text = "NEW UPDATE AVAILABLE (v${appUpdateConfig.latestAppVersion})",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = appUpdateConfig.updateNotes,
                        color = Slate600,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Update link:\n${appUpdateConfig.updateUrl}",
                        color = CyanHighlight,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUpdateConfig.updateUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Opening update link...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenCoin)
                ) {
                    Text("UPDATE NOW", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                if (!appUpdateConfig.isUpdateMandatory) {
                    TextButton(onClick = { isUpdateDialogDismissed = true }) {
                        Text("LATER", color = Slate600)
                    }
                }
            }
        )
    }
}
