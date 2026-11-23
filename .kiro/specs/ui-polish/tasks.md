# Implementation Plan

- [ ] 1. Download and add font files to mobile/src/main/res/font/
  - Add Montserrat variable font (montserrat_variable.ttf)
  - Add RobotoFlex variable font (robotoflex_variable.ttf)
  - Register both via FontFamily declarations in a new Fonts.kt file in core/designsystem
  - _Requirements: 1.1, 1.3, 1.4_


- [ ] 2. Update Type.kt in core/designsystem to use Montserrat and RobotoFlex
  - Replace FontFamily.Default with Montserrat for all sizes
  - Use RobotoFlex at FontWeight(738) for displayLarge only
  - Set FontWeight.W500 as the base weight across all styles
  - _Requirements: 1.1, 1.2_


- [ ] 3. Add per-app brand colors to Color.kt
  - Add instagramReelsColor, youtubeShortsColor, tiktokColor, facebookReelsColor
  - Add holographic accent constants (holographicElectricSky, holographicVioletGlow, holographicNeonMint)
  - Add full light and dark M3 color role tokens following Scrolless naming convention
  - _Requirements: 2.1, 2.2, 3.1, 3.2, 3.3, 3.4_


- [ ] 4. Update Theme.kt to add light scheme, dynamic color, and MaterialExpressiveTheme
  - Add lightScheme and darkScheme using the new color tokens
  - Wrap dynamic color in Build.VERSION.SDK_INT >= Build.VERSION_CODES.S guard
  - Switch from MaterialTheme to MaterialExpressiveTheme with MotionScheme.expressive()
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_


- [ ] 5. Update HomeScreen.kt to use brand colors on donut and add holographic background
  - Replace hardcoded arc colors with instagramReelsColor, youtubeShortsColor, tiktokColor, facebookReelsColor
  - Add a Box with Brush.radialGradient using holographic colors at low alpha behind the content column
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 4.1, 4.2, 4.3_


- [ ] 6. Build and verify
  - Run ./gradlew :mobile:assembleDebug — must pass with zero errors
  - Run ./gradlew :core:designsystem:assembleDebug
  - _Requirements: all_
