package com.example.fsbattle.data

import android.content.Context
import android.util.Log
import com.example.fsbattle.data.local.AppDatabase
import com.example.fsbattle.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class FsBattleRepository private constructor(context: Context) {

    private val appContext: Context = context.applicationContext
    private val db = AppDatabase.getDatabase(appContext)
    private val transactionDao = db.transactionDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        Log.w("FsBattleRepo", "Firebase Auth init fallback: ${e.message}")
        null
    }

    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.w("FsBattleRepo", "Firestore init fallback: ${e.message}")
        null
    }

    // User Authentication & Device Ban State
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isDeviceBanned = MutableStateFlow(false)
    val isDeviceBanned: StateFlow<Boolean> = _isDeviceBanned.asStateFlow()

    // Current User Profile State
    private val _currentUser = MutableStateFlow(
        UserProfile(
            uid = auth?.currentUser?.uid ?: "user_default_101",
            displayName = auth?.currentUser?.displayName ?: "Hassan Gaming",
            gamerTag = "FS_SHADOW_FF",
            email = auth?.currentUser?.email ?: "muhammadhassangamingffpl@gmail.com",
            phone = "+92 300 7654321",
            walletBalance = 1500L,
            winningCoins = 600L,
            depositCoins = 900L,
            totalWon = 4200L,
            totalSpent = 1100L,
            role = "user",
            isSuspended = false,
            banReason = "",
            gameId = "FS_SHADOW_FF",
            gameUid = "88991204"
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Tournament Categories (3-Column Grid)
    private val _categories = MutableStateFlow<List<TournamentCategory>>(
        listOf(
            TournamentCategory("cat_1", "Free Fire", "cat_freefire", 12),
            TournamentCategory("cat_2", "CS 4v4", "cat_cs4v4", 9),
            TournamentCategory("cat_3", "PUBG Mobile", "cat_pubg", 8),
            TournamentCategory("cat_4", "Ludo King", "cat_ludo", 5),
            TournamentCategory("cat_5", "Call of Duty", "cat_cod", 4),
            TournamentCategory("cat_6", "BGMI Esports", "cat_bgmi", 3)
        )
    )
    val categories: StateFlow<List<TournamentCategory>> = _categories.asStateFlow()

    // Home Auto Sliders
    private val _sliders = MutableStateFlow<List<HomeSlider>>(
        listOf(
            HomeSlider("slide_1", "FS BATTLE SEASON 5 ROYALE", "https://example.com/banner1.jpg", "https://wa.me/923001234567"),
            HomeSlider("slide_2", "MEGA CS 4v4 SQUAD WAR 20,000 RS", "https://example.com/banner2.jpg", "https://wa.me/923001234567"),
            HomeSlider("slide_3", "EARN FREE COINS WITH REWARDED ADS", "https://example.com/banner3.jpg", null)
        )
    )
    val sliders: StateFlow<List<HomeSlider>> = _sliders.asStateFlow()

    // Leaderboard Entries (Weekly, Monthly, All-Time)
    private val _leaderboardEntries = MutableStateFlow<List<LeaderboardEntry>>(
        listOf(
            LeaderboardEntry("lb_1", "FS_SHADOW_FF", 1, 14200L, 24, 18, "weekly"),
            LeaderboardEntry("lb_2", "PABLO_GAMING", 2, 11800L, 20, 14, "weekly"),
            LeaderboardEntry("lb_3", "VENOM_FF_PRO", 3, 9500L, 18, 11, "weekly"),
            LeaderboardEntry("lb_4", "ALPHA_SQUAD_IGL", 4, 8200L, 16, 9, "weekly"),
            LeaderboardEntry("lb_5", "FS_VIPER", 5, 7100L, 15, 8, "weekly"),
            
            LeaderboardEntry("lb_6", "FS_SHADOW_FF", 1, 48000L, 88, 62, "monthly"),
            LeaderboardEntry("lb_7", "DRAGON_SLAYER", 2, 42000L, 75, 54, "monthly"),
            LeaderboardEntry("lb_8", "NINJA_FF", 3, 39000L, 70, 49, "monthly"),

            LeaderboardEntry("lb_9", "FS_SHADOW_FF", 1, 180000L, 320, 240, "all_time"),
            LeaderboardEntry("lb_10", "LEGEND_KING", 2, 150000L, 290, 210, "all_time")
        )
    )
    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = _leaderboardEntries.asStateFlow()

    // Tournament Completed Results
    private val _tournamentResults = MutableStateFlow<List<TournamentResultItem>>(
        listOf(
            TournamentResultItem(
                id = "res_1",
                tournamentTitle = "FS BATTLE SOLO SEASON 4",
                category = "Free Fire",
                mapName = "Bermuda",
                totalJoinedPlayers = 48,
                winnerName = "FS_SHADOW_FF",
                winnerKills = 14,
                prizeWon = 2500L,
                scoreboardJson = ""
            ),
            TournamentResultItem(
                id = "res_2",
                tournamentTitle = "SQUAD CS 4v4 TOURNAMENT",
                category = "CS 4v4",
                mapName = "Kalahari",
                totalJoinedPlayers = 16,
                winnerName = "ALPHA SQUAD",
                winnerKills = 28,
                prizeWon = 6000L,
                scoreboardJson = ""
            )
        )
    )
    val tournamentResults: StateFlow<List<TournamentResultItem>> = _tournamentResults.asStateFlow()

    // Earn Coins Unity Ads State
    private val _earnCoinsState = MutableStateFlow(
        EarnCoinsState(
            gameId = "800109391",
            placementId = "Rewarded_Android",
            coinsPerAd = 50L,
            maxAdsPerDay = 5,
            adsWatchedToday = 1,
            cooldownDurationMs = 120000L, // 2 minutes cooldown
            lastAdTimestamp = System.currentTimeMillis() - 150000L
        )
    )
    val earnCoinsState: StateFlow<EarnCoinsState> = _earnCoinsState.asStateFlow()

    // App Version Remote Config State
    private val _appUpdateConfig = MutableStateFlow(AppUpdateConfig())
    val appUpdateConfig: StateFlow<AppUpdateConfig> = _appUpdateConfig.asStateFlow()

    init {
        listenToFirebaseSettings()
    }

    private fun listenToFirebaseSettings() {
        try {
            firestore?.collection("settings")?.document("unity_ads")
                ?.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FsBattleRepo", "Unity ads snapshot listener error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val isEnabled = snapshot.getBoolean("isAdsEnabled") ?: true
                        val isAvailable = snapshot.getBoolean("isAdAvailable") ?: true
                        val gId = snapshot.getString("gameId") ?: "800109391"
                        val reward = snapshot.getLong("coinsPerAd") ?: 50L
                        val maxAds = snapshot.getLong("maxAdsPerDay")?.toInt() ?: 5
                        val cdMs = snapshot.getLong("cooldownDurationMs") ?: (30 * 60 * 1000L)
                        _earnCoinsState.value = _earnCoinsState.value.copy(
                            isAdsEnabled = isEnabled,
                            isAdAvailable = isAvailable,
                            gameId = gId,
                            coinsPerAd = reward,
                            maxAdsPerDay = maxAds,
                            cooldownDurationMs = cdMs
                        )
                    }
                }

            firestore?.collection("settings")?.document("app_update")
                ?.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FsBattleRepo", "App update snapshot listener error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val curVer = snapshot.getString("currentAppVersion") ?: "1.0.0"
                        val latestVer = snapshot.getString("latestAppVersion") ?: "1.0.0"
                        val isMandatory = snapshot.getBoolean("isUpdateMandatory") ?: false
                        val url = snapshot.getString("updateUrl") ?: "https://fsclash.com/download/fs_clash_v1.1.apk"
                        val notes = snapshot.getString("updateNotes") ?: "🚀 Major Update Available!"
                        _appUpdateConfig.value = AppUpdateConfig(
                            currentAppVersion = curVer,
                            latestAppVersion = latestVer,
                            isUpdateMandatory = isMandatory,
                            updateUrl = url,
                            updateNotes = notes
                        )
                    }
                }
        } catch (e: Exception) {
            Log.w("FsBattleRepo", "Firebase settings listener exception: ${e.message}")
        }
    }

    // Initial Sample Tournaments
    private val _tournaments = MutableStateFlow<List<Tournament>>(
        listOf(
            // --- FREE FIRE MATCHES ---
            Tournament(
                id = "tourney_ff_solo",
                name = "FS FREE FIRE SOLO CHAMPIONSHIP",
                description = "Official Free Fire Solo Showdown. Survival & High Kills earn extra points!",
                format = "Solo",
                category = "Free Fire",
                entryFee = 100L,
                prizePool = 2500L,
                maxParticipants = 50,
                currentParticipants = 34,
                status = "registration_open",
                mapName = "Bermuda",
                startDate = "Today, 08:00 PM",
                roomId = "FS-FF-8821",
                roomPassword = "7890",
                rules = "1. No hacks or modified APKs allowed.\n2. Emulator players restricted.\n3. Room details unlock 15 minutes before start.",
                minSlotRequired = 20,
                killReward = 10L,
                survivalReward = 500L,
                survivalRankingRewards = listOf("Top 1: 1000 Coins", "Top 2: 500 Coins", "Top 3: 300 Coins", "Top 4-5: 100 Coins"),
                liveWatchUrl = "https://youtube.com"
            ),
            Tournament(
                id = "tourney_ff_duo",
                name = "FS FREE FIRE DUO DEATHMATCH PRO",
                description = "Pair up with your best teammate and battle 24 duo squads for the title!",
                format = "Duo",
                category = "Free Fire",
                entryFee = 200L,
                prizePool = 5000L,
                maxParticipants = 25,
                currentParticipants = 18,
                status = "registration_open",
                mapName = "Purgatory",
                startDate = "Tomorrow, 09:00 PM",
                roomId = "FS-FF-9042",
                roomPassword = "4412",
                rules = "1. Both teammates must register under squad tag.\n2. Screen recording mandatory for top 3.",
                minSlotRequired = 10,
                killReward = 20L,
                survivalReward = 1000L,
                survivalRankingRewards = listOf("Top 1: 2500 Coins", "Top 2: 1500 Coins", "Top 3: 1000 Coins")
            ),
            Tournament(
                id = "tourney_ff_squad",
                name = "FS FREE FIRE SQUAD SHOWDOWN",
                description = "Full 4-player Squad battle royale on Bermuda Remastered. Top squad takes the jackpot!",
                format = "Squad",
                category = "Free Fire",
                entryFee = 350L,
                prizePool = 10000L,
                maxParticipants = 12,
                currentParticipants = 8,
                status = "registration_open",
                mapName = "Bermuda Remastered",
                startDate = "Today, 10:00 PM",
                roomId = "FS-FF-SQUAD-1",
                roomPassword = "1212",
                rules = "1. Team leader registers all 4 player UIDs.\n2. Voice chat enabled in room.",
                minSlotRequired = 6,
                killReward = 25L,
                survivalReward = 1500L,
                survivalRankingRewards = listOf("Winner Squad: 5000 Coins", "Runner Up: 3000 Coins")
            ),

            // --- CS 4v4 MATCHES ---
            Tournament(
                id = "tourney_cs_squad",
                name = "CS 4v4 MEGA SQUAD WAR",
                description = "Exclusive 4v4 Clash Squad battle! Best of 7 rounds in Bermuda CS arena.",
                format = "Squad",
                category = "CS 4v4",
                entryFee = 300L,
                prizePool = 8000L,
                maxParticipants = 16,
                currentParticipants = 11,
                status = "registration_open",
                mapName = "CS Bermuda",
                startDate = "Today, 08:30 PM",
                roomId = "CS-4V4-9901",
                roomPassword = "5544",
                rules = "1. Custom room CS 7 rounds unlimited ammo OFF.\n2. Gun attributes OFF for fair esports play.",
                minSlotRequired = 8,
                killReward = 15L,
                survivalReward = 2000L,
                survivalRankingRewards = listOf("Winning Squad: 5000 Coins", "Runner-Up: 2500 Coins")
            ),
            Tournament(
                id = "tourney_cs_duo",
                name = "CS 2v2 TACTICAL DUEL",
                description = "2v2 Clash Squad showdown. High speed aim and team coordination test!",
                format = "Duo",
                category = "CS 4v4",
                entryFee = 150L,
                prizePool = 3500L,
                maxParticipants = 16,
                currentParticipants = 9,
                status = "registration_open",
                mapName = "CS Kalahari",
                startDate = "Tomorrow, 07:00 PM",
                roomId = "CS-2V2-3021",
                roomPassword = "8899",
                rules = "1. Headshots only round bonus +100 coins.\n2. Disconnection during round counts as forfeit.",
                minSlotRequired = 8,
                killReward = 20L,
                survivalReward = 1000L,
                survivalRankingRewards = listOf("Top 1 Duo: 2000 Coins", "Top 2 Duo: 1000 Coins")
            ),
            Tournament(
                id = "tourney_cs_solo",
                name = "CS 1v1 SPEED DUEL CHAMPIONS",
                description = "1v1 Solo Clash Squad tournament. Prove your individual 1v1 gun skill!",
                format = "Solo",
                category = "CS 4v4",
                entryFee = 80L,
                prizePool = 1800L,
                maxParticipants = 32,
                currentParticipants = 21,
                status = "registration_open",
                mapName = "CS Iron Cage",
                startDate = "Today, 06:30 PM",
                roomId = "CS-1V1-7712",
                roomPassword = "3321",
                rules = "1. Single elimination bracket.\n2. Winner moves to next stage automatically.",
                minSlotRequired = 10,
                killReward = 10L,
                survivalReward = 800L,
                survivalRankingRewards = listOf("Champion: 1000 Coins", "Finalist: 500 Coins")
            ),

            // --- PUBG MOBILE MATCHES ---
            Tournament(
                id = "tourney_pubg_squad",
                name = "PUBG SQUAD MEGA BATTLEGROUND",
                description = "4v4 Full Squad Royale! Huge cash prize pool + MVP coin reward bonuses.",
                format = "Squad",
                category = "PUBG Mobile",
                entryFee = 400L,
                prizePool = 12000L,
                maxParticipants = 20,
                currentParticipants = 15,
                status = "registration_open",
                mapName = "Erangel",
                startDate = "30 July, 07:30 PM",
                roomId = "PUBG-SQUAD-1004",
                roomPassword = "9988",
                rules = "1. Full team of 4 registered players required.\n2. All team kills calculated +50 coins.",
                minSlotRequired = 10,
                killReward = 50L,
                survivalReward = 2000L,
                survivalRankingRewards = listOf("Top 1: 6000 Coins", "Top 2: 4000 Coins", "Top 3: 2000 Coins")
            ),
            Tournament(
                id = "tourney_pubg_solo",
                name = "PUBG ERANGEL SOLO SURVIVAL",
                description = "Classic Solo Erangel map showdown. 100 players land, 1 winner takes the coin stash!",
                format = "Solo",
                category = "PUBG Mobile",
                entryFee = 120L,
                prizePool = 3000L,
                maxParticipants = 60,
                currentParticipants = 45,
                status = "registration_open",
                mapName = "Erangel",
                startDate = "Tomorrow, 08:00 PM",
                roomId = "PUBG-SOLO-201",
                roomPassword = "6677",
                rules = "1. Mobile devices only.\n2. Submit match end screenshot in app support.",
                minSlotRequired = 25,
                killReward = 15L,
                survivalReward = 1000L,
                survivalRankingRewards = listOf("Winner Chicken Dinner: 1500 Coins", "Top 2-5: 300 Coins")
            ),

            // --- LUDO KING MATCHES ---
            Tournament(
                id = "tourney_ludo_solo",
                name = "WEEKLY LUDO CHAMPIONS 1v1",
                description = "1v1 Quick Ludo tournament. Fast rounds & instant coin prizes!",
                format = "Solo",
                category = "Ludo King",
                entryFee = 50L,
                prizePool = 1200L,
                maxParticipants = 64,
                currentParticipants = 42,
                status = "registration_open",
                mapName = "Classic Board",
                startDate = "31 July, 06:00 PM",
                roomId = "FS-LUDO-552",
                roomPassword = "1122",
                rules = "1. Fair play enforcement active.\n2. Screenshot of final victory required.",
                minSlotRequired = 8,
                killReward = 0L,
                survivalReward = 1200L,
                survivalRankingRewards = listOf("Winner: 1200 Coins")
            ),
            Tournament(
                id = "tourney_ludo_duo",
                name = "LUDO STAR 2v2 TEAM CLASH",
                description = "Partner up in Ludo King 2v2 mode. First team to get 4 tokens home wins!",
                format = "Duo",
                category = "Ludo King",
                entryFee = 100L,
                prizePool = 2200L,
                maxParticipants = 32,
                currentParticipants = 20,
                status = "registration_open",
                mapName = "Quick 2v2 Board",
                startDate = "Tomorrow, 05:00 PM",
                roomId = "FS-LUDO-2V2",
                roomPassword = "7711",
                rules = "1. Share room code with opponent.\n2. Complete within 20 mins.",
                minSlotRequired = 8,
                killReward = 0L,
                survivalReward = 2200L,
                survivalRankingRewards = listOf("Winning Pair: 2200 Coins")
            ),

            // --- CALL OF DUTY & BGMI ---
            Tournament(
                id = "tourney_cod_squad",
                name = "COD 4v4 DOMINATION SQUAD",
                description = "Fast-paced COD Mobile 4v4 Team Deathmatch tournament.",
                format = "Squad",
                category = "Call of Duty",
                entryFee = 250L,
                prizePool = 6000L,
                maxParticipants = 16,
                currentParticipants = 10,
                status = "registration_open",
                mapName = "Firing Range",
                startDate = "Tomorrow, 09:30 PM",
                roomId = "COD-4V4-101",
                roomPassword = "9900",
                rules = "1. Score limit 50 kills.\n2. Operator skills enabled.",
                minSlotRequired = 6,
                killReward = 10L,
                survivalReward = 2500L,
                survivalRankingRewards = listOf("Winner Team: 4000 Coins", "Runner Up: 2000 Coins")
            ),
            Tournament(
                id = "tourney_bgmi_solo",
                name = "BGMI SOLO WARRIORS",
                description = "BGM Esports official solo Erangel clash.",
                format = "Solo",
                category = "BGMI Esports",
                entryFee = 150L,
                prizePool = 3500L,
                maxParticipants = 50,
                currentParticipants = 38,
                status = "registration_open",
                mapName = "Erangel",
                startDate = "Today, 09:00 PM",
                roomId = "BGMI-SOLO-501",
                roomPassword = "4433",
                rules = "1. No iPad view mod allowed.\n2. Live streaming permitted.",
                minSlotRequired = 20,
                killReward = 20L,
                survivalReward = 1000L,
                survivalRankingRewards = listOf("Top 1: 2000 Coins", "Top 2: 1000 Coins")
            )
        )
    )
    val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

    // User Registrations
    private val _userRegistrations = MutableStateFlow<List<TournamentRegistration>>(
        listOf(
            TournamentRegistration(
                id = "reg_101",
                tournamentId = "tourney_1",
                userId = _currentUser.value.uid,
                gamerTag = "FS_SHADOW_FF",
                entryFeePaid = 100L,
                status = "registered"
            )
        )
    )
    val userRegistrations: StateFlow<List<TournamentRegistration>> = _userRegistrations.asStateFlow()

    // Matches schedule
    private val _matches = MutableStateFlow<List<MatchScheduleItem>>(
        listOf(
            MatchScheduleItem(
                id = "match_1",
                tournamentId = "tourney_1",
                tournamentName = "FS BATTLE SOLO CHAMPIONSHIP",
                format = "Solo",
                matchNumber = 1,
                mapName = "Bermuda",
                scheduledTime = "Today, 08:00 PM",
                status = "scheduled",
                roomId = "FS-ROOM-8821",
                roomPassword = "7890"
            )
        )
    )
    val matches: StateFlow<List<MatchScheduleItem>> = _matches.asStateFlow()

    // Transactions
    val transactions: Flow<List<CachedTransaction>> = transactionDao.getAllTransactions()

    // Deposits & Withdrawals
    private val _deposits = MutableStateFlow<List<DepositRequest>>(emptyList())
    val deposits: StateFlow<List<DepositRequest>> = _deposits.asStateFlow()

    private val _withdrawals = MutableStateFlow<List<WithdrawalRequest>>(emptyList())
    val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif_1",
                userId = _currentUser.value.uid,
                title = "Welcome to FS CLASH!",
                body = "Your account is verified. Enjoy esports tournaments & coin rewards.",
                type = "info",
                read = false,
                timestamp = System.currentTimeMillis() - 3600000
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    init {
        scope.launch {
            val initial = listOf(
                CachedTransaction(
                    id = "tx_101",
                    userId = _currentUser.value.uid,
                    type = "deposit",
                    title = "Easypaisa Deposit Approved",
                    amount = 500L,
                    balanceAfter = 1500L,
                    status = "completed",
                    gateway = "Easypaisa",
                    timestamp = System.currentTimeMillis() - 86400000
                ),
                CachedTransaction(
                    id = "tx_102",
                    userId = _currentUser.value.uid,
                    type = "fee_deduction",
                    title = "Entry Fee: FS BATTLE SOLO",
                    amount = 100L,
                    balanceAfter = 1400L,
                    status = "completed",
                    timestamp = System.currentTimeMillis() - 1800000
                )
            )
            transactionDao.insertAll(initial)
        }
    }

    // --- AUTH ACTIONS ---

    fun loginUser(email: String, gamerTag: String) {
        val user = _currentUser.value.copy(
            email = email,
            gamerTag = if (gamerTag.isNotBlank()) gamerTag else _currentUser.value.gamerTag,
            displayName = if (gamerTag.isNotBlank()) gamerTag else _currentUser.value.displayName
        )
        _currentUser.value = user
        _isLoggedIn.value = true
    }

    fun registerUser(email: String, gamerTag: String, pass: String) {
        val user = _currentUser.value.copy(
            email = email,
            gamerTag = gamerTag,
            displayName = gamerTag,
            walletBalance = 1000L,
            depositCoins = 1000L,
            winningCoins = 0L
        )
        _currentUser.value = user
        _isLoggedIn.value = true
    }

    fun logoutUser() {
        _isLoggedIn.value = false
    }

    // --- USER PANEL ACTIONS ---

    suspend fun registerForTournament(
        tournament: Tournament,
        teamName: String = "",
        teamMembers: List<String> = emptyList()
    ): Result<String> {
        val user = _currentUser.value
        if (user.walletBalance < tournament.entryFee) {
            return Result.failure(Exception("Insufficient wallet balance (${user.walletBalance} Coins). Deposit coins to join!"))
        }

        if (_userRegistrations.value.any { it.tournamentId == tournament.id }) {
            return Result.failure(Exception("You are already registered for this match!"))
        }

        val newBalance = user.walletBalance - tournament.entryFee
        val newDepositCoins = (user.depositCoins - tournament.entryFee).coerceAtLeast(0L)
        val updatedUser = user.copy(
            walletBalance = newBalance,
            depositCoins = newDepositCoins,
            totalSpent = user.totalSpent + tournament.entryFee
        )
        _currentUser.value = updatedUser

        val reg = TournamentRegistration(
            id = "reg_" + UUID.randomUUID().toString().take(8),
            tournamentId = tournament.id,
            userId = user.uid,
            gamerTag = user.gamerTag,
            teamName = teamName,
            teamMembers = teamMembers,
            entryFeePaid = tournament.entryFee,
            status = "registered"
        )
        _userRegistrations.value = _userRegistrations.value + reg

        _tournaments.value = _tournaments.value.map {
            if (it.id == tournament.id) {
                it.copy(currentParticipants = it.currentParticipants + 1)
            } else it
        }

        val tx = CachedTransaction(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            type = "fee_deduction",
            title = "Entry Fee: ${tournament.name}",
            amount = tournament.entryFee,
            balanceAfter = newBalance,
            status = "completed"
        )
        transactionDao.insertTransaction(tx)

        val notif = NotificationItem(
            id = "notif_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            title = "Joined ${tournament.name}",
            body = "Entry fee ${tournament.entryFee} Coins deducted. Room ID & Password unlocked!",
            type = "registration"
        )
        _notifications.value = listOf(notif) + _notifications.value
        com.example.fsbattle.utils.NotificationHelper.showSystemNotification(appContext, notif.title, notif.body)

        return Result.success("Joined Match! ${tournament.entryFee} Coins deducted from wallet.")
    }

    suspend fun requestDeposit(amount: Long, gateway: String, trxId: String): Result<String> {
        if (amount < 50) return Result.failure(Exception("Minimum deposit is 50 Coins"))
        if (trxId.isBlank()) return Result.failure(Exception("Please enter a valid Transaction ID"))

        val deposit = DepositRequest(
            id = "dep_" + UUID.randomUUID().toString().take(8),
            userId = _currentUser.value.uid,
            amount = amount,
            gateway = gateway,
            trxId = trxId,
            status = "pending"
        )
        _deposits.value = listOf(deposit) + _deposits.value

        val notif = NotificationItem(
            id = "notif_" + UUID.randomUUID().toString().take(8),
            userId = _currentUser.value.uid,
            title = "Deposit Submitted ($amount Coins)",
            body = "Your $gateway deposit request (Trx: $trxId) was submitted. Approval pending admin review.",
            type = "wallet"
        )
        _notifications.value = listOf(notif) + _notifications.value
        com.example.fsbattle.utils.NotificationHelper.showSystemNotification(appContext, notif.title, notif.body)

        return Result.success("Deposit request for $amount Coins submitted via $gateway!")
    }

    suspend fun requestWithdrawal(
        amount: Long,
        gateway: String,
        accountNumber: String,
        accountTitle: String
    ): Result<String> {
        val user = _currentUser.value
        if (amount < 100) return Result.failure(Exception("Minimum withdrawal is 100 Coins"))
        if (user.walletBalance < amount) {
            return Result.failure(Exception("Insufficient wallet balance (${user.walletBalance} Coins)"))
        }
        if (accountNumber.isBlank() || accountTitle.isBlank()) {
            return Result.failure(Exception("Please enter Account Number and Account Title"))
        }

        val newBalance = user.walletBalance - amount
        val newWinningCoins = (user.winningCoins - amount).coerceAtLeast(0L)
        _currentUser.value = user.copy(walletBalance = newBalance, winningCoins = newWinningCoins)

        val wd = WithdrawalRequest(
            id = "wd_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            amount = amount,
            gateway = gateway,
            accountNumber = accountNumber,
            accountTitle = accountTitle,
            status = "pending"
        )
        _withdrawals.value = listOf(wd) + _withdrawals.value

        val tx = CachedTransaction(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            type = "withdrawal",
            title = "Withdrawal Request ($gateway)",
            amount = amount,
            balanceAfter = newBalance,
            status = "pending",
            gateway = gateway
        )
        transactionDao.insertTransaction(tx)

        val notif = NotificationItem(
            id = "notif_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            title = "Withdrawal Request ($amount Coins)",
            body = "Withdrawal request to $gateway ($accountNumber) submitted.",
            type = "wallet"
        )
        _notifications.value = listOf(notif) + _notifications.value

        return Result.success("Withdrawal request for $amount Coins submitted!")
    }

    suspend fun convertWinningCoins(winningAmount: Long): Result<String> {
        val user = _currentUser.value
        if (winningAmount <= 0) return Result.failure(Exception("Please enter a valid amount"))
        if (user.winningCoins < winningAmount) {
            return Result.failure(Exception("Insufficient winning coins balance (${user.winningCoins} C)"))
        }

        // Ratio: 1000 winning coins = 100 deposit coins (10:1 ratio)
        val depositCoinsEarned = winningAmount / 10
        if (depositCoinsEarned <= 0) return Result.failure(Exception("Minimum 10 Winning Coins required to convert!"))

        val newWinning = user.winningCoins - winningAmount
        val newDeposit = user.depositCoins + depositCoinsEarned
        val newWallet = newWinning + newDeposit

        _currentUser.value = user.copy(
            winningCoins = newWinning,
            depositCoins = newDeposit,
            walletBalance = newWallet
        )

        val tx = CachedTransaction(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            type = "convert",
            title = "Converted $winningAmount Winning Coins",
            amount = depositCoinsEarned,
            balanceAfter = newWallet,
            status = "completed"
        )
        transactionDao.insertTransaction(tx)

        return Result.success("Converted $winningAmount Winning Coins into $depositCoinsEarned Deposit Coins!")
    }

    suspend fun watchAdReward(): Result<Long> {
        val state = _earnCoinsState.value

        if (!state.isAdsEnabled) {
            return Result.failure(Exception("Ads feature is currently disabled by Admin."))
        }

        if (!state.isAdAvailable) {
            return Result.failure(Exception("Ads Not Available! No video loaded. No coins rewarded."))
        }

        val now = System.currentTimeMillis()
        val nextAvailable = state.lastAdTimestamp + state.cooldownDurationMs

        if (state.adsWatchedToday >= state.maxAdsPerDay) {
            return Result.failure(Exception("Daily ad limit reached (${state.maxAdsPerDay}/${state.maxAdsPerDay})!"))
        }

        if (now < nextAvailable) {
            val remainingSec = (nextAvailable - now) / 1000
            return Result.failure(Exception("Reward cooldown active! Please wait $remainingSec seconds."))
        }

        val reward = state.coinsPerAd
        val user = _currentUser.value
        val newBalance = user.walletBalance + reward
        val newWinning = user.winningCoins + reward

        _currentUser.value = user.copy(
            walletBalance = newBalance,
            winningCoins = newWinning,
            totalWon = user.totalWon + reward
        )

        _earnCoinsState.value = state.copy(
            adsWatchedToday = state.adsWatchedToday + 1,
            lastAdTimestamp = now
        )

        val tx = CachedTransaction(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            userId = user.uid,
            type = "ad_reward",
            title = "Unity Ads Reward (+${reward} Coins)",
            amount = reward,
            balanceAfter = newBalance,
            status = "completed"
        )
        transactionDao.insertTransaction(tx)

        // Firebase Firestore Realtime Sync
        try {
            firestore?.collection("users")?.document(user.uid)?.set(
                mapOf(
                    "walletBalance" to newBalance,
                    "winningCoins" to newWinning,
                    "totalWon" to (user.totalWon + reward),
                    "lastAdTimestamp" to now,
                    "adsWatchedToday" to (_earnCoinsState.value.adsWatchedToday)
                )
            )
            firestore?.collection("transactions")?.document(tx.id)?.set(
                mapOf(
                    "id" to tx.id,
                    "userId" to user.uid,
                    "type" to tx.type,
                    "title" to tx.title,
                    "amount" to tx.amount,
                    "balanceAfter" to tx.balanceAfter,
                    "status" to tx.status,
                    "timestamp" to now
                )
            )
        } catch (e: Exception) {
            Log.w("FsBattleRepo", "Firebase transaction sync notice: ${e.message}")
        }

        return Result.success(reward)
    }

    fun saveAdSettingsToFirebase() {
        try {
            val state = _earnCoinsState.value
            firestore?.collection("settings")?.document("unity_ads")?.set(
                mapOf(
                    "isAdsEnabled" to state.isAdsEnabled,
                    "isAdAvailable" to state.isAdAvailable,
                    "gameId" to state.gameId,
                    "placementId" to state.placementId,
                    "interstitialPlacementId" to state.interstitialPlacementId,
                    "bannerPlacementId" to state.bannerPlacementId,
                    "coinsPerAd" to state.coinsPerAd,
                    "maxAdsPerDay" to state.maxAdsPerDay,
                    "cooldownDurationMs" to state.cooldownDurationMs,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Log.w("FsBattleRepo", "Firebase save ad settings notice: ${e.message}")
        }
    }

    fun toggleAdsEnabled(enabled: Boolean) {
        _earnCoinsState.value = _earnCoinsState.value.copy(isAdsEnabled = enabled)
        saveAdSettingsToFirebase()
    }

    fun toggleAdAvailability(available: Boolean) {
        _earnCoinsState.value = _earnCoinsState.value.copy(isAdAvailable = available)
        saveAdSettingsToFirebase()
    }

    fun updateAdSettings(gameId: String, reward: Long, maxPerDay: Int, cooldownMinutes: Long) {
        _earnCoinsState.value = _earnCoinsState.value.copy(
            gameId = gameId.ifBlank { "800109391" },
            coinsPerAd = reward,
            maxAdsPerDay = maxPerDay,
            cooldownDurationMs = cooldownMinutes * 60 * 1000L
        )
        saveAdSettingsToFirebase()
    }

    fun updateAppVersionConfig(latestVersion: String, updateUrl: String, updateNotes: String, isMandatory: Boolean) {
        val config = _appUpdateConfig.value.copy(
            latestAppVersion = latestVersion.ifBlank { "1.0.0" },
            updateUrl = updateUrl.ifBlank { "https://fsclash.com/download/fs_clash_v1.1.apk" },
            updateNotes = updateNotes.ifBlank { "New version update" },
            isUpdateMandatory = isMandatory
        )
        _appUpdateConfig.value = config

        try {
            firestore?.collection("settings")?.document("app_update")?.set(
                mapOf(
                    "currentAppVersion" to config.currentAppVersion,
                    "latestAppVersion" to config.latestAppVersion,
                    "isUpdateMandatory" to config.isUpdateMandatory,
                    "updateUrl" to config.updateUrl,
                    "updateNotes" to config.updateNotes,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Log.w("FsBattleRepo", "Firebase app update config error: ${e.message}")
        }
    }

    fun updateGamerTag(gamerTag: String, gameId: String, gameUid: String) {
        _currentUser.value = _currentUser.value.copy(
            gamerTag = if (gamerTag.isNotBlank()) gamerTag else _currentUser.value.gamerTag,
            gameId = if (gameId.isNotBlank()) gameId else _currentUser.value.gameId,
            gameUid = if (gameUid.isNotBlank()) gameUid else _currentUser.value.gameUid
        )
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(read = true) else it
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    companion object {
        @Volatile
        private var INSTANCE: FsBattleRepository? = null

        fun getInstance(context: Context): FsBattleRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FsBattleRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
