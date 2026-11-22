package com.hexscroll.app.ui.home
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexscroll.app.accessibility.HexScrollAccessibilityService
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.repository.UserSettingsStore
import com.hexscroll.app.util.isAccessibilityServiceEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userSettingsStore: UserSettingsStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            combine(
                userSettingsStore.getBlockOptionFlow(),
                userSettingsStore.getTimeLimitFlow(),
                userSettingsStore.getTimerOverlayEnabledFlow(),
                userSettingsStore.getPauseUntilFlow(),
                userSettingsStore.getLearningGateEnabledFlow(),
                userSettingsStore.getSelectedTopicsFlow(),
            ) { v ->
                val bo = v[0] as BlockOption; val tl = v[1] as Long; val to = v[2] as Boolean
                val pu = v[3] as Long; val ge = v[4] as Boolean
                @Suppress("UNCHECKED_CAST") val tp = v[5] as Set<LearningTopic>
                listOf(bo, tl, to, pu, ge, tp)
            }.collect { v ->
                @Suppress("UNCHECKED_CAST")
                _uiState.update { s -> s.copy(
                    hasLoadedSettings = true,
                    blockOption = v[0] as BlockOption,
                    timeLimit = v[1] as Long,
                    timerOverlayEnabled = v[2] as Boolean,
                    pauseUntil = v[3] as Long,
                    learningGateEnabled = v[4] as Boolean,
                    selectedTopics = v[5] as Set<LearningTopic>,
                )}
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(
                hasSeenAccessibilityExplainer = userSettingsStore.hasSeenAccessibilityExplainer(),
                hasCompletedTopicOnboarding = userSettingsStore.hasCompletedTopicOnboarding(),
                intervalAllowance = userSettingsStore.getIntervalAllowance(),
                intervalLength = userSettingsStore.getIntervalLength(),
                hauntCooldownUntil = userSettingsStore.getHauntCooldownUntil(),
                totalDailyUsageMillis = userSettingsStore.getTotalDailyUsage(),
            )}
        }
    }
    fun refreshAccessibilityStatus() {
        _uiState.update { it.copy(isAccessibilityServiceActive = context.isAccessibilityServiceEnabled(HexScrollAccessibilityService::class.java)) }
    }
    fun setBlockOption(o: BlockOption) { viewModelScope.launch { userSettingsStore.setBlockOption(o) } }
    fun setTimeLimit(m: Long) { viewModelScope.launch { userSettingsStore.setTimeLimit(m) } }
    fun setIntervalSettings(allowance: Long, brk: Long) { viewModelScope.launch { userSettingsStore.setIntervalAllowance(allowance); userSettingsStore.setIntervalLength(brk) } }
    fun setTimerOverlayEnabled(e: Boolean) { viewModelScope.launch { userSettingsStore.setTimerOverlayEnabled(e) } }
    fun setLearningGateEnabled(e: Boolean) { viewModelScope.launch { userSettingsStore.setLearningGateEnabled(e) } }
    fun markAccessibilityExplainerSeen() { viewModelScope.launch { userSettingsStore.setHasSeenAccessibilityExplainer(true) } }
    // Debug functions — only called from DebugSessionPanel in DEBUG builds
    fun debugSimulateQuiz() { viewModelScope.launch { userSettingsStore.setHauntCooldownUntil(0L) } }
    fun debugSimulateHaunt() { viewModelScope.launch { userSettingsStore.setHauntCooldownUntil(System.currentTimeMillis() + 180_000L) } }
    fun debugSimulateCooldown() { viewModelScope.launch { userSettingsStore.setHauntCooldownUntil(System.currentTimeMillis() + 60_000L) } }
    fun debugInjectUsage() { viewModelScope.launch { userSettingsStore.setTotalDailyUsage(userSettingsStore.getTotalDailyUsage() + 300_000L) } }
}
