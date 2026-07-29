package com.example.fsbattle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserProfile(
    val uid: String = "",
    val displayName: String = "Player1",
    val gamerTag: String = "FS_SHADOW_FF",
    val email: String = "player1@fsbattle.com",
    val phone: String = "+92 300 1234567",
    val gameId: String = "77210943",
    val gameUid: String = "90123812",
    val profileImageUrl: String = "",
    val role: String = "user", // "user" or "admin"
    val walletBalance: Long = 1500L,
    val depositCoins: Long = 700L,
    val winningCoins: Long = 800L,
    val totalWon: Long = 4200L,
    val totalSpent: Long = 1100L,
    val totalMatches: Int = 24,
    val totalWins: Int = 12,
    val joinedMatchesCount: Int = 18,
    val isBanned: Boolean = false,
    val banNotice: String? = null,
    val isSuspended: Boolean = false,
    val banReason: String = "Violation of Fair Play Rules",
    val deviceId: String = "android_dev_device_9981",
    val isDeviceBanned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class HomeSlider(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val webLink: String? = null
)

data class TournamentCategory(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val tournamentCount: Int = 0
)

data class Tournament(
    val id: String = "",
    val name: String = "",
    val category: String = "Free Fire",
    val description: String = "",
    val format: String = "Solo", // "Solo", "Duo", "Squad"
    val entryFee: Long = 100L,
    val prizePool: Long = 2000L,
    val maxParticipants: Int = 100,
    val currentParticipants: Int = 0,
    val minSlotRequired: Int = 10,
    val killReward: Long = 10L,
    val survivalReward: Long = 50L,
    val survivalRankingRewards: List<String> = listOf(
        "Rank 1: 500 Coins",
        "Rank 2: 300 Coins",
        "Rank 3: 150 Coins",
        "Rank 4: 100 Coins",
        "Rank 5: 50 Coins"
    ),
    val status: String = "registration_open", // "upcoming", "registration_open", "in_progress", "completed"
    val mapName: String = "Bermuda",
    val startDate: String = "Today, 08:00 PM",
    val endDate: String = "Today, 09:30 PM",
    val roomId: String? = null,
    val roomPassword: String? = null,
    val rules: String = "1. No hacks or modified APKs allowed.\n2. Emulator players restricted unless specified.\n3. Join room 15 minutes before start.\n4. Screen recording required for Top 3.",
    val organizer: String = "FS CLASH Official",
    val liveWatchUrl: String? = "https://youtube.com/live/demo"
)

data class TournamentRegistration(
    val id: String = "",
    val tournamentId: String = "",
    val userId: String = "",
    val gamerTag: String = "",
    val gameId: String = "",
    val gameUid: String = "",
    val teamName: String = "",
    val iglGameId: String = "",
    val iglUid: String = "",
    val teamMembers: List<String> = emptyList(),
    val entryFeePaid: Long = 0L,
    val registeredAt: Long = System.currentTimeMillis(),
    val status: String = "registered" // "registered", "verified", "disqualified"
)

@Entity(tableName = "cached_transactions")
data class CachedTransaction(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String, // "deposit", "withdrawal", "fee_deduction", "reward", "ad_reward", "coin_conversion"
    val title: String,
    val amount: Long,
    val balanceAfter: Long,
    val status: String, // "completed", "pending", "rejected"
    val gateway: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class DepositRequest(
    val id: String = "",
    val userId: String = "",
    val amount: Long = 0L,
    val gateway: String = "Easypaisa", // "Easypaisa", "JazzCash", "UPaisa", "Other"
    val trxId: String = "",
    val accountHolder: String = "",
    val status: String = "pending", // "pending", "accepted", "rejected"
    val timestamp: Long = System.currentTimeMillis()
)

data class WithdrawalRequest(
    val id: String = "",
    val userId: String = "",
    val amount: Long = 0L,
    val gateway: String = "Easypaisa", // "Easypaisa", "JazzCash", "UPaisa", "Other"
    val accountNumber: String = "",
    val accountHolder: String = "",
    val accountTitle: String = "",
    val status: String = "pending",
    val timestamp: Long = System.currentTimeMillis()
)

data class MatchScheduleItem(
    val id: String = "",
    val tournamentId: String = "",
    val tournamentName: String = "",
    val category: String = "Free Fire",
    val format: String = "Solo",
    val matchNumber: Int = 1,
    val mapName: String = "Bermuda",
    val scheduledTime: String = "Today, 08:00 PM",
    val status: String = "scheduled", // "scheduled", "live", "completed"
    val winnerName: String? = null,
    val scores: String? = null,
    val roomId: String? = null,
    val roomPassword: String? = null
)

data class MatchPlayerResult(
    val rank: Int,
    val username: String,
    val kills: Int,
    val coinsWon: Long
)

data class MatchResultItem(
    val id: String = "",
    val matchTitle: String = "",
    val category: String = "Free Fire",
    val mapName: String = "Bermuda",
    val completedDate: String = "Yesterday, 09:00 PM",
    val rankings: List<MatchPlayerResult> = emptyList()
)

data class TournamentResultItem(
    val id: String = "",
    val tournamentTitle: String = "",
    val category: String = "Free Fire",
    val mapName: String = "Bermuda",
    val totalJoinedPlayers: Int = 48,
    val winnerName: String = "FS_SHADOW_FF",
    val winnerKills: Int = 14,
    val prizeWon: Long = 2500L,
    val scoreboardJson: String = ""
)

data class LeaderboardEntry(
    val id: String = "",
    val username: String = "",
    val rank: Int = 1,
    val totalCoins: Long = 0L,
    val totalWins: Int = 0,
    val matchesPlayed: Int = 0,
    val timeframe: String = "weekly" // "weekly", "monthly", "all_time"
)

data class NotificationItem(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "info", // "registration", "room", "wallet", "reward", "promotional", "maintenance"
    val read: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class EarnCoinsState(
    val isAdsEnabled: Boolean = true, // Admin master switch to enable/disable ads feature
    val isAdAvailable: Boolean = true, // Ad load status: true = ready, false = "Ads Not Available"
    val gameId: String = "800109391",
    val placementId: String = "Rewarded_Android",
    val interstitialPlacementId: String = "Interstitial_Android",
    val bannerPlacementId: String = "Banner_Android",
    val coinsPerAd: Long = 50L,
    val maxAdsPerDay: Int = 5,
    val adsWatchedToday: Int = 1,
    val lastAdTimestamp: Long = 0L,
    val cooldownDurationMs: Long = 30 * 60 * 1000L
)

data class AppInfoData(
    val aboutText: String = "FS CLASH is Pakistan's premier esports tournament platform for Free Fire, PUBG Mobile, and competitive mobile gaming. Compete in daily Solo, Duo, and Squad battles, win coin rewards, and instantly manage deposits & withdrawals.",
    val websiteUrl: String = "https://fsclash.com",
    val privacyText: String = "We take user privacy seriously. FS CLASH collects basic account details (Email, Gamer Tag, Game ID) strictly to manage tournament registrations and wallet payouts. Data is secured via Firebase Cloud Security.",
    val privacyUrl: String = "https://fsclash.com/privacy",
    val termsText: String = "By using FS CLASH, you agree to fair-play policies. Use of third-party cheats, emulators in mobile-only matches, or fake payment receipts will result in immediate account suspension and device ban.",
    val termsUrl: String = "https://fsclash.com/terms",
    val halalPolicyText: String = "Halal Gaming & Fair-Play Guidelines:\n1. Games of skill & competitive tournament skill matches are hosted with transparent entry fees and prize distribution.\n2. Gambling, chance games, and unethical betting are strictly prohibited.\n3. All coins earned or deposited are tracked transparently without interest.",
    val halalPolicyUrl: String = "https://fsclash.com/halal-policy"
)

data class AppUpdateConfig(
    val currentAppVersion: String = "1.0.0",
    val latestAppVersion: String = "1.0.0",
    val isUpdateMandatory: Boolean = false,
    val updateUrl: String = "https://fsclash.com/download/fs_clash_v1.1.apk",
    val updateNotes: String = "🚀 Major Update v1.1.0:\n• Enhanced Unity Ads Rewards Integration\n• Realtime Firebase Cloud Synchronization\n• Faster tournament room loading & instant withdrawal processing!"
)
