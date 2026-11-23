# Design Document

## Data Model Changes
The existing QuestionEntity and LearningTopic enum are fixed for built-in packs. Custom packs need a new approach.

### New entity: CustomTopicEntity (table: custom_topics)
- id: String (UUID, primary key)
- name: String (user-entered topic name)
- subtitle: String (optional description)
- createdAt: Long (epoch millis)
- questionCount: Int (cached count, updated on insert/delete)

### Extended QuestionEntity
Add a nullable column customTopicId: String? — null for built-in questions, UUID for custom topic questions.
This requires a Room MIGRATION_1_2.

### LearningGateCoordinator change
evaluateBlock() must also check if the user has selected any custom topic IDs and query custom questions from the extended QuestionEntity.

### UserSettingsStore change
selectedTopics currently stores comma-separated LearningTopic.id strings. Extend to also store custom topic UUIDs in the same field.

## JSON Schema for Import and Export
Same schema as the existing bundled packs. The topic field is the custom topic name as a string.
Each entry has: id (string), topic (string), prompt (string), options (4 strings), correctIndex (0-3), explanation (string)

## New Screens

### TopicPickerScreen changes
- Existing built-in topic grid stays at top
- Below it: a My Custom Topics section with a list of custom topic rows
- Each row shows: name, question count badge, Edit button, Delete button
- Add Custom Topic button at bottom of the section

### QuestionEditorScreen (new)
- Route: topic_editor/{customTopicId}
- LazyColumn of all questions for this topic
- FAB opens AddQuestionDialog
- Top bar: topic name, Import JSON button, Export button

### AddQuestionDialog (new)
- AlertDialog with scrollable form
- Fields: Prompt (multiline), Options A B C D (TextFields), Correct answer (radio A/B/C/D), Explanation
- Validate on save: no blank fields, all options distinct
- On save: insert QuestionEntity with customTopicId, increment count
