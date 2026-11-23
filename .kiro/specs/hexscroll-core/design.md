# Design Document

## Architecture
Clean Architecture + MVVM with 5 Gradle modules: core:logging, core:domain, core:data, core:designsystem, mobile.

## Blocking Engine Design
BlockingManagerImpl holds an active BlockOptionHandler (Strategy pattern). Switches handler reactively via collectLatest on UserSettingsStore.getBlockOptionFlow(). The 1-second periodicCheck() in HexScrollAccessibilityService calls blockingManager.onPeriodicCheck(elapsed, now) and routes the result.

## Learning Gate Design
LearningGateCoordinatorImpl.evaluateBlock() checks in order: gate enabled -> cooldown active -> topics selected -> question available. Returns a sealed LearningGateDecision (PassThrough, ShowQuiz, ShowHauntLockout).

## Overlay Engine Design
ChallengeOverlayManager adds a ComposeView to WindowManager using TYPE_ACCESSIBILITY_OVERLAY. The ComposeView is wrapped in BackLockFrameLayout which consumes KEYCODE_BACK. OverlayLifecycleOwner provides the required ViewTreeLifecycleOwner and ViewTreeSavedStateRegistryOwner. ChallengeOverlayContent is a state machine between Quiz and Haunting states using AnimatedContent.

## Data Layer Design
Single Room database (hexscroll.db v1) with 3 entities: UserSettingsEntity (single-row id=1), SessionSegmentEntity, QuestionEntity. UserSettingsStoreImpl caches all settings in MutableStateFlows hydrated on init. QuestionBankSeeder uses a Mutex and seeds from bundled JSON assets once on first launch.

## Home Screen Design
UsageDonut composable draws 4 segmented arcs (one per app) using Canvas with animateFloatAsState. Mode cards are tappable Card composables that highlight CrimsonAccent when selected. Dialogs (TimeLimitDialog, IntervalTimerDialog) use Material3 AlertDialog with Slider + preset FilterChip rows.
