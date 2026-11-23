# Requirements Document

## Introduction
HexScroll needs a README.md that serves as the first thing any visitor or developer sees on the GitHub repository. It must clearly explain what the app does, how to install it, how to fix the most common issue users face (accessibility service being killed in the background by aggressive OEM battery optimisers), and how to build from source. The README must also reference the .kiro specification directory to show the project was built with Kiro.

## Glossary
- **OEM battery optimiser**: A phone manufacturer's background-kill system that terminates services to save battery (Infinix, Xiaomi, Huawei, Samsung, Oppo, Vivo)
- **Accessibility Service**: The Android system service HexScroll requires to monitor and block feeds
- **Sideloading**: Installing an APK outside the Play Store, requiring Install unknown apps to be enabled

## Requirement 1: README content
**User Story:** As a developer or user visiting the GitHub repository, I want to immediately understand what HexScroll does and how to get it running so that I can start using or contributing to it.

### Acceptance Criteria
1. WHEN a visitor opens the repository, the README SHALL display the app name, tagline, and a one-paragraph description above the fold
2. WHEN a visitor reads the README, it SHALL reference the .kiro directory and explain it contains the spec that guided development
3. WHEN a visitor reads the README, it SHALL list all blocking modes and the Learning Gate feature with brief descriptions
4. WHEN a user wants to install the app, the README SHALL provide numbered step-by-step install instructions including how to enable Install unknown apps
5. WHEN a developer wants to build from source, the README SHALL provide the exact gradle commands to build and test

## Requirement 2: Background service warning section
**User Story:** As a user on an Infinix, Xiaomi, Samsung, or other OEM phone, I want clear instructions for keeping the accessibility service alive so that HexScroll does not stop blocking after I clear recent apps.

### Acceptance Criteria
1. WHEN a user reads the README, it SHALL contain a dedicated section titled Keeping the accessibility service alive
2. WHEN a user reads that section, it SHALL explain why the service gets killed (OEM background app killing)
3. WHEN a user reads that section, it SHALL provide the universal fix (toggle accessibility off and on, allow background activity)
4. WHEN a user reads that section, it SHALL provide phone-specific steps for Infinix/Tecno/itel, Xiaomi/Redmi/POCO, Huawei/Honor, Oppo/Realme/OnePlus, Vivo/iQOO, and Samsung
5. WHEN a user on an Infinix phone tries to install the APK and sees blocked to protect your device, the README SHALL explain this is the Install unknown apps restriction and how to bypass it
