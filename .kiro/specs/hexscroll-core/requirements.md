# Requirements Document

## Introduction
HexScroll is an Android accessibility-service-based app that blocks short-form video feeds (Instagram Reels, YouTube Shorts, TikTok, Facebook Reels) and optionally gates access behind topic-scoped learning questions.

## Glossary
- **BlockableApp**: An enum representing a supported app/surface that can be blocked
- **Learning Gate**: The optional quiz interception layer before a blocked feed is exited
- **Haunting Screen**: The penalty screen shown on quiz failure, featuring glitch effects and spooky media
- **Grace Window**: The timed access period (default 5 min) granted on quiz pass
- **Cooldown**: The lockout period (default 3 min) set after quiz failure

## Requirement 1: Feed Blocking
**User Story:** As a user, I want to block short-form video feeds so that I spend less time doomscrolling.

### Acceptance Criteria
1. WHEN Block All is active AND a blocked feed opens, THE service SHALL exit the feed within 1 second
2. WHEN Daily Limit is active AND cumulative usage reaches the limit, THE service SHALL block for the rest of the day
3. WHEN Interval Timer is active AND the usage window is exhausted, THE service SHALL block until the break period ends
4. WHEN NothingSelected is active, THE service SHALL never block any app

## Requirement 2: Learning Gate
**User Story:** As a user, I want to answer a question before accessing a blocked feed so that I am nudged to learn instead of scroll.

### Acceptance Criteria
1. WHEN Learning Gate is enabled AND a block triggers, THE overlay SHALL show one MCQ from the selected topics
2. WHEN the user answers correctly, THE app SHALL grant a 5-minute grace window and dismiss the overlay
3. WHEN the user answers incorrectly OR the 20-second timer expires, THE overlay SHALL transition to the Haunting Screen
4. WHEN the user is in cooldown AND tries to open a blocked feed, THE app SHALL skip the quiz and show the Haunting Screen immediately

## Requirement 3: Haunting Screen
**User Story:** As a user who answered wrong, I want an unskippable penalty screen so that wrong answers have real consequences.

### Acceptance Criteria
1. WHEN the Haunting Screen shows, THE screen SHALL display a random image from the haunt asset pool
2. WHEN the Haunting Screen shows, THE app SHALL play a random sound at alarm volume regardless of ringer mode
3. WHEN the user presses Back during the Haunting Screen, THE app SHALL consume the event and not dismiss
4. WHEN the 5-second countdown ends, THE app SHALL exit the feed and set the cooldown

## Requirement 4: Usage Tracking
**User Story:** As a user, I want to see how much time I spend per app so that I am aware of my habits.

### Acceptance Criteria
1. WHEN a session ends, THE app SHALL record duration per app to the database
2. WHEN a new day starts, THE app SHALL reset all daily usage counters
3. WHEN the home screen is open, THE donut chart SHALL show today's per-app usage as animated arcs
