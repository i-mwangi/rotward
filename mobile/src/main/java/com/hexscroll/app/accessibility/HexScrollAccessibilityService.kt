package com.hexscroll.app.accessibility
import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import com.hexscroll.app.core.blocking.BlockingManager
import com.hexscroll.app.core.blocking.LearningGateCoordinator
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.BlockingResult
import com.hexscroll.app.core.model.ExitStrategy
import com.hexscroll.app.core.model.LearningGateDecision
import com.hexscroll.app.core.repository.SessionTracker
import com.hexscroll.app.core.repository.UserSettingsStore
import com.hexscroll.app.ui.overlay.ChallengeOverlayManager
import com.hexscroll.app.ui.overlay.TimerOverlayManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HexScrollAccessibilityService : AccessibilityService() {
    @Inject lateinit var sessionTracker: SessionTracker
    @Inject lateinit var blockingManager: BlockingManager
    @Inject lateinit var learningGateCoordinator: LearningGateCoordinator
    @Inject lateinit var userSettingsStore: UserSettingsStore
    @Inject lateinit var timerOverlayManager: TimerOverlayManager
    @Inject lateinit var challengeOverlayManager: ChallengeOverlayManager
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var detectedApp: BlockableApp? = null
    private var timeStartedMillis: Long = 0L
    private var isChallengeActive = false
    private var cachedPauseUntil: Long = 0L
    private var cachedTimerOverlayEnabled: Boolean = true
    private var cachedTimerX: Int = 0
    private var cachedTimerY: Int = 120

    private fun scheduleNextCheck() { mainHandler.postDelayed(::periodicCheck, 1000) }

    private fun periodicCheck() {
        val app = detectedApp ?: return
        if (isChallengeActive) { scheduleNextCheck(); return }
        val now = System.currentTimeMillis()
        val elapsed = now - timeStartedMillis
        if (now < cachedPauseUntil) {
            val remaining = cachedPauseUntil - now
            if (cachedTimerOverlayEnabled) timerOverlayManager.update(remaining)
            scheduleNextCheck(); return
        }
        serviceScope.launch {
            val result = blockingManager.onPeriodicCheck(elapsed, now)
            withContext(Dispatchers.Main) {
                when (result) {
                    is BlockingResult.Block -> handleBlockTriggered(app)
                    is BlockingResult.AllowWithRemaining -> {
                        if (cachedTimerOverlayEnabled) {
                            timerOverlayManager.show(result.remainingMillis, cachedTimerX, cachedTimerY)
                            timerOverlayManager.update(result.remainingMillis)
                        }
                        scheduleNextCheck()
                    }
                    BlockingResult.Allow -> scheduleNextCheck()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        timerOverlayManager.attachServiceContext(this)
        challengeOverlayManager.attachServiceContext(this)
        serviceScope.launch { userSettingsStore.getPauseUntilFlow().collectLatest { cachedPauseUntil = it } }
        serviceScope.launch { userSettingsStore.getTimerOverlayEnabledFlow().collectLatest { cachedTimerOverlayEnabled = it } }
        serviceScope.launch { cachedTimerX = userSettingsStore.getTimerOverlayX(); cachedTimerY = userSettingsStore.getTimerOverlayY() }
        Timber.d("HexScrollAccessibilityService created")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        if (isChallengeActive) return
        val packageName = event.packageName?.toString() ?: return
        val app = BlockableApp.fromPackage(packageName) ?: run { if (detectedApp != null) onExitBlockedContent(); return }
        if (detectedApp == null) onEnterBlockedContent(app)
    }

    private fun onEnterBlockedContent(app: BlockableApp) {
        detectedApp = app; timeStartedMillis = System.currentTimeMillis()
        serviceScope.launch { sessionTracker.onAppOpen(app) }
        scheduleNextCheck()
        Timber.d("Entered blocked content: ${app.name}")
    }

    private fun onExitBlockedContent() {
        val app = detectedApp ?: return
        mainHandler.removeCallbacksAndMessages(null)
        timerOverlayManager.hide()
        val elapsed = System.currentTimeMillis() - timeStartedMillis
        serviceScope.launch {
            sessionTracker.addToDailyUsage(app, elapsed)
            blockingManager.onExitBlockedContent(elapsed, System.currentTimeMillis())
            sessionTracker.onAppClose()
        }
        detectedApp = null; timeStartedMillis = 0L
        Timber.d("Exited blocked content: ${app.name}")
    }

    private fun handleBlockTriggered(app: BlockableApp) {
        if (isChallengeActive) return
        isChallengeActive = true
        mainHandler.removeCallbacksAndMessages(null)
        timerOverlayManager.hide()
        serviceScope.launch {
            when (val decision = learningGateCoordinator.evaluateBlock()) {
                LearningGateDecision.PassThrough -> {
                    isChallengeActive = false
                    withContext(Dispatchers.Main) { performExitNavigation(app) }
                }
                is LearningGateDecision.ShowQuiz -> {
                    challengeOverlayManager.showQuiz(
                        question = decision.question,
                        targetApp = app,
                        onPassed = {
                            serviceScope.launch {
                                learningGateCoordinator.onQuizPassed(decision.question.id)
                                withContext(Dispatchers.Main) {
                                    challengeOverlayManager.dismiss()
                                    isChallengeActive = false
                                    scheduleNextCheck()
                                }
                            }
                        },
                        onHauntFinishedAfterFail = {
                            serviceScope.launch {
                                learningGateCoordinator.onQuizFailed(decision.question.id)
                                withContext(Dispatchers.Main) {
                                    challengeOverlayManager.dismiss()
                                    isChallengeActive = false
                                    performExitNavigation(app)
                                }
                            }
                        },
                    )
                }
                is LearningGateDecision.ShowHauntLockout -> {
                    challengeOverlayManager.showCooldownHaunt(
                        remainingMillis = decision.remainingMillis,
                        onFinished = {
                            challengeOverlayManager.dismiss()
                            isChallengeActive = false
                            performExitNavigation(app)
                        },
                    )
                }
            }
        }
    }

    private fun performExitNavigation(app: BlockableApp) {
        mainHandler.post {
            when (app.exitAction) {
                ExitStrategy.HOME -> performGlobalAction(GLOBAL_ACTION_HOME)
                ExitStrategy.BACK -> performGlobalAction(GLOBAL_ACTION_BACK)
            }
        }
    }

    override fun onInterrupt() {
        Timber.d("Service interrupted")
        mainHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
        timerOverlayManager.detach()
        challengeOverlayManager.dismiss()
        serviceScope.cancel()
        Timber.d("HexScrollAccessibilityService destroyed")
    }
}
