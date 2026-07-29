package com.example.fsbattle.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fsbattle.data.FsBattleRepository
import com.example.fsbattle.data.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FsBattleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FsBattleRepository.getInstance(application)

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    val isDeviceBanned: StateFlow<Boolean> = repository.isDeviceBanned
    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val tournaments: StateFlow<List<Tournament>> = repository.tournaments
    val categories: StateFlow<List<TournamentCategory>> = repository.categories
    val sliders: StateFlow<List<HomeSlider>> = repository.sliders
    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = repository.leaderboardEntries
    val tournamentResults: StateFlow<List<TournamentResultItem>> = repository.tournamentResults
    val earnCoinsState: StateFlow<EarnCoinsState> = repository.earnCoinsState
    val appUpdateConfig: StateFlow<AppUpdateConfig> = repository.appUpdateConfig

    val userRegistrations: StateFlow<List<TournamentRegistration>> = repository.userRegistrations
    val matches: StateFlow<List<MatchScheduleItem>> = repository.matches
    val deposits: StateFlow<List<DepositRequest>> = repository.deposits
    val withdrawals: StateFlow<List<WithdrawalRequest>> = repository.withdrawals
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications

    val transactions: StateFlow<List<CachedTransaction>> = repository.transactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedFormatFilter = MutableStateFlow("All") // "All", "Solo", "Duo", "Squad"
    val selectedFormatFilter: StateFlow<String> = _selectedFormatFilter.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("All")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    val filteredTournaments: StateFlow<List<Tournament>> = combine(
        tournaments,
        selectedFormatFilter,
        selectedCategoryFilter
    ) { list, format, category ->
        list.filter { item ->
            val matchFormat = format == "All" || item.format.equals(format, ignoreCase = true)
            val matchCategory = category == "All" || item.category.equals(category, ignoreCase = true)
            matchFormat && matchCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Message / Toast / Snackbar State
    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage.asSharedFlow()

    fun setFormatFilter(filter: String) {
        _selectedFormatFilter.value = filter
    }

    fun setCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    fun loginUser(email: String, gamerTag: String) {
        repository.loginUser(email, gamerTag)
        viewModelScope.launch {
            _uiMessage.emit("Logged in successfully!")
        }
    }

    fun registerUser(email: String, gamerTag: String, pass: String) {
        repository.registerUser(email, gamerTag, pass)
        viewModelScope.launch {
            _uiMessage.emit("Account created! 1000 Coins credited.")
        }
    }

    fun logoutUser() {
        repository.logoutUser()
        viewModelScope.launch {
            _uiMessage.emit("Logged out.")
        }
    }

    fun registerForTournament(tournament: Tournament, teamName: String = "", teamMembers: List<String> = emptyList()) {
        viewModelScope.launch {
            val result = repository.registerForTournament(tournament, teamName, teamMembers)
            result.onSuccess { msg ->
                _uiMessage.emit(msg)
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Registration failed")
            }
        }
    }

    fun requestDeposit(amount: Long, gateway: String, trxId: String) {
        viewModelScope.launch {
            val result = repository.requestDeposit(amount, gateway, trxId)
            result.onSuccess { msg ->
                _uiMessage.emit(msg)
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Deposit request failed")
            }
        }
    }

    fun requestWithdrawal(amount: Long, gateway: String, accountNumber: String, accountTitle: String) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(amount, gateway, accountNumber, accountTitle)
            result.onSuccess { msg ->
                _uiMessage.emit(msg)
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Withdrawal request failed")
            }
        }
    }

    fun convertCoins(winningAmount: Long) {
        viewModelScope.launch {
            val result = repository.convertWinningCoins(winningAmount)
            result.onSuccess { msg ->
                _uiMessage.emit(msg)
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Coin conversion failed")
            }
        }
    }

    fun watchAdReward() {
        viewModelScope.launch {
            val result = repository.watchAdReward()
            result.onSuccess { coins ->
                _uiMessage.emit("🎉 Rewarded $coins Coins! Added to wallet.")
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Ad reward unavailable")
            }
        }
    }

    fun toggleAdsEnabled(enabled: Boolean) {
        repository.toggleAdsEnabled(enabled)
        viewModelScope.launch {
            _uiMessage.emit(if (enabled) "Ads Feature ENABLED for users" else "Ads Feature DISABLED for users (Hidden from Userpanel)")
        }
    }

    fun toggleAdAvailability(available: Boolean) {
        repository.toggleAdAvailability(available)
        viewModelScope.launch {
            _uiMessage.emit(if (available) "Unity Ads Status: AVAILABLE (Ready to Stream)" else "Unity Ads Status: NOT AVAILABLE (No Fill / Error)")
        }
    }

    fun updateAdSettings(gameId: String, reward: Long, maxPerDay: Int, cooldownMinutes: Long) {
        repository.updateAdSettings(gameId, reward, maxPerDay, cooldownMinutes)
        viewModelScope.launch {
            _uiMessage.emit("Unity Ads settings updated and synced to Firebase!")
        }
    }

    fun updateAppVersionConfig(latestVersion: String, updateUrl: String, updateNotes: String, isMandatory: Boolean) {
        repository.updateAppVersionConfig(latestVersion, updateUrl, updateNotes, isMandatory)
        viewModelScope.launch {
            _uiMessage.emit("🚀 App Update v$latestVersion published & synced to Firebase!")
        }
    }

    fun updateGamerTag(gamerTag: String, gameId: String, gameUid: String) {
        repository.updateGamerTag(gamerTag, gameId, gameUid)
        viewModelScope.launch {
            _uiMessage.emit("Gamer credentials updated!")
        }
    }

    fun markNotificationRead(id: String) {
        repository.markNotificationRead(id)
    }

    fun clearNotifications() {
        repository.clearNotifications()
    }
}
