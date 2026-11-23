# Code Policy

## Module Boundaries
- Never add Android UI imports to core:domain
- Never add Room or domain imports to core:designsystem
- Use KSP, never kapt

## Threading
- WindowManager.addView / removeView must run on Dispatchers.Main
- Room queries must run on Dispatchers.IO
- Await setPauseUntil() write before dismissing overlay to prevent re-trigger race

## Overlay Safety
- ComposeView in AccessibilityService requires OverlayLifecycleOwner before setContent()
- Lifecycle must advance ON_CREATE, ON_START, ON_RESUME in order
- Always wrap WindowManager.removeView in try/catch

## Colors
- Background: #050507, Cards: #111116, Crimson: #FF2A4B, Emerald: #22C55E
- Dark-only app, never use light theme colors
