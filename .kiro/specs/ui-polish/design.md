# Design Document

## Typography Design
Following Scrolless exactly: Montserrat for all sizes (display through label), RobotoFlex only for displayLarge with FontWeight(738) for impact. FontWeight.W500 is the default weight across all Montserrat styles — this gives a consistently medium-weight feel that is cleaner than the default W400.

Font files needed:
- Montserrat-VariableFont_wght.ttf (variable font covers all weights)
- RobotoFlex-VariableFont_GRAD,XTRA,YOPQ,YTAS,YTDE,YTFI,YTLC,YTUC,opsz,slnt,wdth,wght.ttf
Both are available from Google Fonts as free open-source fonts.

## Color System Design
Dark scheme (HexScroll branded, used as fallback):
- background: #111318 (Scrolless dark background)
- surface: #111318
- surfaceContainer: #1D2024
- surfaceContainerHigh: #282A2F
- primary: #FF2A4B (HexScroll crimson — kept as brand identity)
- onPrimary: #FFFFFF
- onBackground: #E2E2E9
- onSurface: #E2E2E9
- outline: #8E9099

Light scheme (new — HexScroll did not have one before):
- background: #F9F9FF
- surface: #F9F9FF
- primary: #C4003B (darker crimson for light theme contrast)
- onBackground: #191C20

Dynamic color: wrap in Build.VERSION.SDK_INT >= S check, use dynamicDarkColorScheme / dynamicLightColorScheme.

## Per-App Brand Colors
Replace the current hardcoded Color(0xFF4FC3F7) etc. with named constants:
- instagramReelsColor = #E1306C
- youtubeShortsColor = #FFB300
- tiktokColor = #00F2EA
- facebookReelsColor = #1877F2

## Holographic Ambient Background
A Box behind the Column in HomeScreen with a radial brush using three colors at low alpha:
- Electric sky: #58C7FF at 8% alpha
- Violet glow: #9C7BFF at 6% alpha
- Neon mint: #5DFFB3 at 5% alpha
Rendered as Brush.radialGradient centered at top-center. In light mode use 3% alpha max.
