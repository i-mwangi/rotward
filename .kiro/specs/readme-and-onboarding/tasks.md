# Implementation Plan

- [x] 1. Write README.md at repository root
  - App name: HexScroll
  - Tagline: Block Reels and Shorts and other forms of addictive content
  - One paragraph product description
  - _Requirements: 1.1_


- [x] 2. Add Built with Kiro section
  - Explain that Kiro (https://kiro.dev) wrote the spec before the code was written
  - List all .kiro spec files with one-line descriptions
  - List hooks and steering files
  - _Requirements: 1.2_


- [x] 3. Add What it does section
  - Block All, Daily Limit, Interval Timer, Learning Gate, Haunting Screen, Usage Tracking, Timer Overlay
  - Supported apps: Instagram Reels, YouTube Shorts, TikTok, Facebook Reels
  - _Requirements: 1.3_


- [x] 4. Add Install section with sideload warning
  - Note that 'blocked to protect your device' on Infinix means Install unknown apps is off, not a virus warning
  - Steps: download APK from Releases, enable Install unknown apps, install, enable accessibility service
  - State minimum requirement: Android 8.0 (API 26)
  - _Requirements: 1.4, 2.5_


- [x] 5. Add Keeping the accessibility service alive section
  - Explain OEM background app killing causes the service to stop
  - Universal fix: toggle accessibility off and on, allow background activity
  - Phone-specific table: Infinix/Tecno/itel, Xiaomi/Redmi/POCO, Huawei/Honor, Oppo/Realme/OnePlus, Vivo/iQOO, Samsung
  - _Requirements: 2.1, 2.2, 2.3, 2.4_


- [x] 6. Add Tech stack and Build from source sections
  - Tech stack one-liners: Kotlin 2.x, Compose, Hilt, Room, Coil, Timber, 100% offline
  - Build command: ./gradlew :mobile:assembleDebug
  - Test command: ./gradlew :core:domain:test :core:data:testDebugUnitTest
  - _Requirements: 1.5_


- [ ]* 7. Add screenshots to README
  - HomeScreen showing donut ring and mode cards
  - Quiz overlay screen
  - Haunting screen
  - TopicPickerScreen
  - _Requirements: 1.1_
