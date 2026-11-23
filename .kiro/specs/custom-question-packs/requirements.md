# Requirements Document

## Introduction
HexScroll's Learning Gate currently only uses 6 built-in topic packs (120 questions total). This feature allows users to create their own custom question packs and topics — uploading their own study material so the Learning Gate doubles as a revision tool. A user studying for an exam, learning a language, or preparing a presentation can load their own questions and be tested on them every time they try to open a blocked feed.

## Glossary
- **Custom Pack**: A user-created question set imported from a JSON file or typed manually in-app
- **Custom Topic**: A user-defined topic name (e.g. 'KCSE Biology', 'French Vocab Week 3', 'AWS Certification')
- **Pack Import**: Loading a JSON file from device storage that matches the question schema
- **Pack Export**: Saving a custom pack as a JSON file to share or back up
- **Question Editor**: The in-app screen for creating and editing individual questions

## Requirement 1: Create custom topics
**User Story:** As a student, I want to create my own topic so that the Learning Gate can test me on subjects I am actually studying.

### Acceptance Criteria
1. WHEN the user opens Topic Picker, they SHALL see an Add Custom Topic button alongside the built-in topics
2. WHEN the user taps Add Custom Topic, they SHALL be able to enter a topic name and optional subtitle
3. WHEN a custom topic is saved, it SHALL appear in the Topic Picker alongside the built-in topics
4. WHEN a custom topic has zero questions, the Topic Picker SHALL show a warning badge and block confirming until at least 2 questions are added
5. WHEN a custom topic is deleted, all its questions SHALL be deleted from the database

## Requirement 2: Add questions manually
**User Story:** As a user, I want to type my own questions and answers in-app so that I can build a revision pack without needing to edit JSON files.

### Acceptance Criteria
1. WHEN the user taps a custom topic, they SHALL see a Question Editor screen listing all questions in that topic
2. WHEN the user taps Add Question, they SHALL see a form with fields: Prompt, Option A, Option B, Option C, Option D, Correct Answer (A/B/C/D), Explanation
3. WHEN the user submits a question, the app SHALL validate that all fields are non-blank and correct answer index is 0-3
4. WHEN a question is saved, it SHALL immediately be available in the Learning Gate for that topic
5. WHEN the user long-presses a question, they SHALL be able to edit or delete it

## Requirement 3: Import a question pack from JSON
**User Story:** As a user, I want to import a JSON file of questions from my phone storage so that I can prepare packs on my computer and load them into HexScroll.

### Acceptance Criteria
1. WHEN the user taps Import Pack on a custom topic, they SHALL be shown a file picker for JSON files
2. WHEN a valid JSON file is selected, the app SHALL parse it and insert questions using the standard schema
3. WHEN a JSON file contains invalid entries, the app SHALL import valid questions and report how many were skipped
4. WHEN importing, existing questions with matching IDs SHALL be skipped (no duplicates)
5. WHEN the JSON schema does not match, the app SHALL show a clear error message with the expected format

## Requirement 4: Export a custom pack
**User Story:** As a user, I want to export my custom pack as a JSON file so that I can back it up or share it with others.

### Acceptance Criteria
1. WHEN the user taps Export Pack, the app SHALL write the topic's questions as a JSON file to the Downloads folder
2. WHEN the export completes, the app SHALL show a snackbar with the file path
3. WHEN the exported file is re-imported into HexScroll on another device, it SHALL work without modification
