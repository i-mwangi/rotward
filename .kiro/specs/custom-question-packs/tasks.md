# Implementation Plan

- [ ] 1. Add CustomTopicEntity and migrate Room database to version 2
  - Create CustomTopicEntity with id, name, subtitle, createdAt, questionCount
  - Add nullable customTopicId column to QuestionEntity
  - Write MIGRATION_1_2 with ALTER TABLE and CREATE TABLE SQL
  - Add CustomTopicDao with insert, delete, getAll, getById, updateCount
  - Register new entity and migration in HexScrollDatabase, bump version to 2
  - _Requirements: 1.1, 1.5, 2.4_


- [ ] 2. Create CustomTopicRepository and extend LearningGate
  - Add CustomTopicRepository interface and impl in core:data
  - Extend UserSettingsStore to store selected custom topic UUIDs alongside enum IDs
  - Update LearningGateCoordinatorImpl to query custom topic questions when selected
  - Update QuestionDao to support WHERE customTopicId IN query
  - _Requirements: 1.3, 2.4_


- [ ] 3. Update TopicPickerScreen with custom topic section
  - Add My Custom Topics section below built-in topics grid
  - Each custom topic row shows name, count badge, Edit and Delete buttons
  - Add Custom Topic dialog: name field (required), subtitle field (optional)
  - Custom topics with zero questions show warning badge and block confirmation
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5_


- [ ] 4. Build QuestionEditorScreen
  - New screen at route topic_editor/{customTopicId}
  - LazyColumn listing all questions with prompt preview and correct answer label
  - Long-press on question shows Edit and Delete bottom sheet
  - Top bar shows topic name, Import JSON icon button, Export icon button
  - FAB opens AddQuestionDialog
  - _Requirements: 2.1, 2.5_


- [ ] 5. Build AddQuestionDialog with full validation
  - Form: Prompt (multiline), Options A B C D, Correct answer radio group, Explanation
  - Validate: no blank fields, correct index 0-3, options must all be distinct
  - On save: insert QuestionEntity with new UUID, increment topic questionCount
  - On edit: update existing QuestionEntity by id
  - _Requirements: 2.2, 2.3, 2.4_


- [ ] 6. Implement JSON import from device storage
  - Use ActivityResultContracts.OpenDocument with mimeType application/json
  - Parse with kotlinx.serialization.json, validate each entry
  - Insert valid questions, skip duplicates by ID, show summary snackbar
  - Show error dialog with the expected schema when format is wrong
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_


- [ ] 7. Implement JSON export to Downloads folder
  - Serialize all custom topic questions to the standard JSON schema
  - Write to Downloads/{topicName}_questions.json using MediaStore on API 29+
  - Show snackbar with the file path on success
  - _Requirements: 4.1, 4.2, 4.3_


- [ ] 8. Build and verify
  - Run ./gradlew :core:domain:test :core:data:testDebugUnitTest :mobile:assembleDebug
  - Test: create topic, add questions, block TikTok, verify custom question appears in overlay
  - Test: export pack to Downloads, import it back, verify no duplicates
  - _Requirements: all_
