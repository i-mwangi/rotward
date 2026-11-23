# Implementation Plan

- [x] 1. Gradle scaffold and module structure
  - Root build.gradle.kts, settings.gradle.kts, gradle.properties, libs.versions.toml
  - Empty build.gradle.kts for all 5 modules with correct dependency rules
  - Gradle wrapper and typesafe project accessors
  - _Requirements: N/A (infrastructure)_


- [x] 2. core:logging module
  - DebugLogTree with file/line stack element tagging
  - ReleaseLogTree suppressing below WARN
  - _Requirements: N/A (infrastructure)_


- [x] 3. core:domain — models, blocking engine, learning gate
  - BlockableApp, BlockOption, BlockingResult, LearningTopic, Question, LearningGateDecision models
  - BlockOptionHandler strategy interface with NoBlock, BlockAll, DayLimit, IntervalTimer implementations
  - BlockingManagerImpl singleton switching handlers reactively on BlockOption flow
  - LearningGateCoordinatorImpl with evaluateBlock, onQuizPassed, onQuizFailed
  - UserSettingsStore, SessionTracker, SessionSegmentStore, QuestionRepository interfaces
  - Unit tests: BlockAllBlockHandlerTest, DayLimitBlockHandlerTest, IntervalTimerBlockHandlerTest, NoBlockHandlerTest, LearningGateCoordinatorTest
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 2.1, 2.2, 2.3, 2.4_


- [x] 4. core:data — Room database and question packs
  - HexScrollDatabase v1 with UserSettingsEntity, SessionSegmentEntity, QuestionEntity
  - UserSettingsDao, SessionSegmentDao, QuestionDao with spaced-repetition query ordering
  - UserSettingsStoreImpl with MutableStateFlow cache hydrated on init via INSERT OR IGNORE
  - SessionTrackerImpl with daily reset detection
  - QuestionBankSeeder with Mutex and INSERT IGNORE to preserve user stats
  - 6 JSON question packs (120 MCQs): Programming, Languages, History, Science, Math, Personal Finance
  - QuestionPackValidationTest verifying all 7 invariants across all packs
  - _Requirements: 1.2, 1.3, 2.1, 4.1, 4.2_


- [x] 5. core:designsystem — theme and visual primitives
  - Pure-black dark Material 3 color scheme (Background #050507, Crimson #FF2A4B, Emerald #22C55E)
  - HauntColors, HauntEffects: CrtScanlineOverlay, GlitchOffsetText (chromatic aberration), ScreenFlickerBox
  - SegmentedCircularProgress with animated per-app arcs and AppUsageLegend
  - AnimatedButton with press-scale animation, AutoResizingText
  - _Requirements: 3.1, 4.3_


- [x] 6. mobile — app shell and navigation
  - HexScrollApplication with Hilt + Timber initialization
  - MainActivity with edge-to-edge and HexScrollTheme
  - NavHost with Screen.Home and Screen.TopicPicker routes
  - _Requirements: N/A (infrastructure)_


- [x] 7. TopicPickerScreen
  - 2x3 selectable dark card grid for all 6 LearningTopic entries
  - Live counter badge enforcing MIN_SELECTION=2 and MAX_SELECTION=5
  - Snackbar for 6th-topic attempt, disabled Confirm below 2 selections
  - Saves selectedTopics and sets hasCompletedTopicOnboarding on confirm
  - _Requirements: 2.1_


- [x] 8. HomeScreen with UsageDonut and mode cards
  - UsageDonut: Canvas-drawn 4-segment animated ring with per-app arcs and center total
  - Mode cards: Block All, Daily Limit, Interval, Off as tappable crimson-highlighted cards
  - TimeLimitDialog with slider and preset chips; IntervalTimerDialog with dual sliders
  - Status pill showing grace window or cooldown remaining time
  - AccessibilityExplainerDialog on first launch; AccessibilitySuccessBottomSheet on enable
  - HelpDialog explaining all modes; DebugSessionPanel in DEBUG builds only
  - _Requirements: 1.1, 1.2, 1.3, 2.1, 4.3_


- [x] 9. HexScrollAccessibilityService — detection and blocking loop
  - Package + viewId hint detection for Reels, Shorts, TikTok, Facebook Reels
  - 1-second periodicCheck() scheduling via Handler/Looper
  - Grace-window check before blocking to prevent re-trigger race
  - LearningGateCoordinator evaluation routing to quiz, haunt, or passthrough
  - performExitNavigation() using GLOBAL_ACTION_BACK or GLOBAL_ACTION_HOME
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 2.1, 2.2, 2.3, 2.4, 4.1, 4.2_


- [x] 10. OverlayLifecycleOwner and overlay window management
  - OverlayLifecycleOwner implementing LifecycleOwner + SavedStateRegistryOwner
  - SavedStateRegistryController.performAttach() and performRestore(null) before ON_CREATE
  - Lifecycle advanced ON_CREATE, ON_START, ON_RESUME on show; reversed on dismiss
  - BackLockFrameLayout consuming KEYCODE_BACK and KEYCODE_ESCAPE
  - _Requirements: 3.3_


- [x] 11. ChallengeOverlayContent — quiz and haunting state machine
  - Quiz state: 20s depleting timer bar, topic pill, 4 answer options with 700ms feedback flash
  - Correct answer: emerald flash with Correct message, then onPassed()
  - Wrong answer / timeout: crimson flash, transitions to Haunting state in-place
  - Haunting state: GlitchOffsetText headline, random Coil image (PNG+GIF), random STREAM_ALARM sound
  - ChallengeOverlayManager: TYPE_ACCESSIBILITY_OVERLAY window, haptic waveform
  - _Requirements: 2.2, 2.3, 3.1, 3.2, 3.3, 3.4_


- [x] 12. TimerOverlayManager — floating countdown
  - FLAG_NOT_FOCUSABLE + FLAG_NOT_TOUCHABLE so taps pass through to the blocked app
  - Compose-rendered countdown badge with ObsidianCard background
  - mutableLongStateOf for real-time update without recreating the window
  - _Requirements: 1.2, 1.3_


- [x] 13. Haunt media assets and custom launcher icon
  - 11 images in mobile/src/main/assets/haunt/images/ (PNG + GIF including logo.gif)
  - 5 sounds in mobile/src/main/assets/haunt/sounds/ (MP3)
  - Launcher icon resized to all 5 mipmap densities (48..192dp) from 2048x2048 source
  - ic_launcher_round for circular icon phones
  - _Requirements: 3.1, 3.2_


- [ ] 14. Timeline bar on HomeScreen
  - Horizontal bar showing session blocks by hour of day (00:00-24:00)
  - Per-app color coding matching the donut arcs
  - Collapsible with session count and total duration header
  - _Requirements: 4.3_


- [ ] 15. Weekly average bar chart
  - Mon-Sun bar chart from SessionSegment data aggregated by day
  - Last week / this week toggle
  - _Requirements: 4.3_


- [ ]* 16. Signed release APK and Play Store listing
  - Generate and store a release keystore (never commit)
  - ./gradlew :mobile:bundleRelease for AAB
  - Privacy policy page (required for Accessibility Service apps)
  - Store listing: 512x512 icon, 2+ screenshots, description
  - _Requirements: N/A (distribution)_
