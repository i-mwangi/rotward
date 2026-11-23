# Project Structure

## Modules
- core:logging   - Timber trees only
- core:domain    - Pure Kotlin, models, blocking handlers, LearningGateCoordinator. No Android UI or Room.
- core:data      - Room DB, DAOs, question packs (120 MCQs), UserSettingsStoreImpl
- core:designsystem - Compose Material 3, HauntEffects, SegmentedCircularProgress. No domain/data deps.
- mobile         - App shell, HomeScreen, TopicPickerScreen, AccessibilityService, overlay engine

## Key Paths
- core/data/src/main/assets/questions/ - 6 JSON question packs
- core/data/src/main/assets/haunt/     - spooky images and sounds
- mobile/src/main/.../ui/overlay/      - ChallengeOverlayManager, ChallengeOverlayContent
- mobile/src/main/.../accessibility/   - HexScrollAccessibilityService
- mobile/src/main/res/mipmap-*/        - launcher icons (crimson brain+padlock)
