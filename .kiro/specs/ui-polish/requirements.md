# Requirements Document

## Introduction
HexScroll's current UI uses FontFamily.Default and a flat crimson-only dark theme. This spec upgrades the visual design drawing from Scrolless — a polished open-source Android app with the same purpose. The goal is a more professional, readable app that still keeps HexScroll's dark crimson identity while adding proper Material3 color roles, the Montserrat font, per-app brand colors, and dynamic color support on Android 12+.

## Glossary
- **Montserrat**: Google Font used by Scrolless for all UI text — clean, modern, highly readable at small sizes
- **Dynamic color**: Android 12+ feature where the app color scheme is derived from the user's wallpaper
- **M3 color roles**: Material Design 3 semantic color tokens (primary, surface, container, etc.) that work correctly in both light and dark themes
- **Per-app brand colors**: Distinct colors per blocked app matching each platform's brand identity

## Requirement 1: Montserrat Typography
**User Story:** As a user, I want the app text to feel polished and readable so that the app looks professional.

### Acceptance Criteria
1. WHEN the app renders any text, it SHALL use Montserrat as the primary font family
2. WHEN display-size text is shown, the app SHALL use RobotoFlex at high font weight for impact
3. WHEN Montserrat is not available, the app SHALL fall back to FontFamily.Default
4. WHEN the font files are added, they SHALL be placed in mobile/src/main/res/font/ and registered via a FontFamily declaration

## Requirement 2: Full Material3 Light and Dark Color Schemes
**User Story:** As a user, I want the app to respect my system theme and look great in both light and dark mode.

### Acceptance Criteria
1. WHEN the system is in dark mode, the app SHALL use a dark color scheme with background #111318
2. WHEN the system is in light mode, the app SHALL use a light color scheme with background #F9F9FF
3. WHEN running on Android 12 or higher, the app SHALL use dynamic color derived from the wallpaper
4. WHEN dynamic color is not available, the app SHALL fall back to the HexScroll branded scheme
5. WHEN the dark scheme is active, crimson #FF2A4B SHALL remain as the primary brand accent

## Requirement 3: Per-App Brand Colors on Donut Chart
**User Story:** As a user viewing my usage donut chart, I want each app segment to use that app's real brand color so I can instantly identify which arc belongs to which app.

### Acceptance Criteria
1. WHEN the donut chart renders, Instagram Reels arc SHALL use #E1306C
2. WHEN the donut chart renders, YouTube Shorts arc SHALL use #FFB300
3. WHEN the donut chart renders, TikTok arc SHALL use #00F2EA
4. WHEN the donut chart renders, Facebook Reels arc SHALL use #1877F2

## Requirement 4: Holographic ambient background on HomeScreen
**User Story:** As a user on the home screen, I want a subtle ambient gradient background so the screen feels alive and premium, not flat.

### Acceptance Criteria
1. WHEN the HomeScreen renders in dark mode, it SHALL show a subtle radial gradient using holographic accent colors behind the content
2. WHEN the HomeScreen renders in light mode, the gradient SHALL be very subtle and not clash with text readability
3. WHEN the gradient is applied, it SHALL NOT obscure any UI element — it is a background effect only
