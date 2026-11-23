# Design Document

## README Structure
The README.md lives at the repository root. It is the landing page on GitHub. Sections in order:

1. App name and tagline
2. Built with Kiro section - links to .kiro directory and lists spec files
3. What it does - feature list (blocking modes, Learning Gate, Haunting Screen, usage tracking)
4. Install - numbered steps including the sideload warning for Infinix and other OEM phones
5. Keeping the accessibility service alive - universal fix then phone-specific table
6. Tech stack - one-liners
7. Build from source - gradle commands

## Kiro Section Design
The Built with Kiro section appears second, immediately after the tagline. It explains that Kiro wrote the spec before the code and lists each .kiro file with a one-line description. This gives the section credibility and shows the .kiro directory was not added as an afterthought.

## Background Service Section Design
This is the most practically important section for OEM phone users. Layout:
- One sentence explaining why it happens (OEM battery killers)
- Numbered universal fix (2 steps)
- Markdown table with one row per manufacturer grouping: Infinix/Tecno/itel, Xiaomi/Redmi/POCO, Huawei/Honor, Oppo/Realme/OnePlus, Vivo/iQOO, Samsung
- Each row has the manufacturer name and exact menu path/toggle names

## Install Section Design
Addresses the common Infinix error 'blocked to protect your device' upfront. Explains this is the Install unknown apps restriction, not a virus warning. Steps:
1. Download APK from Releases link
2. Settings then Security then Install unknown apps - enable for WhatsApp or file manager
3. Open APK and tap Install
4. Open HexScroll, complete topic picker, enable accessibility service
