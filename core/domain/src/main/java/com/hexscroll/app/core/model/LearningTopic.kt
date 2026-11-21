package com.hexscroll.app.core.model

enum class LearningTopic(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val assetFileName: String,
) {
    PROGRAMMING("PROGRAMMING", "Programming & CS", "Algorithms, Kotlin, systems, Git", "programming.json"),
    LANGUAGES("LANGUAGES", "Languages", "Spanish & French vocabulary and grammar", "languages.json"),
    HISTORY("HISTORY", "World History", "Civilizations, turning points, dates", "history.json"),
    SCIENCE("SCIENCE", "General Science", "Physics, biology, chemistry fundamentals", "science.json"),
    MATH("MATH", "Math & Logic", "Mental math, probability, algebra", "math.json"),
    PERSONAL_FINANCE("PERSONAL_FINANCE", "Personal Finance", "Compounding, budgeting, risk, taxes", "personal_finance.json");

    companion object {
        const val MIN_SELECTION = 2
        const val MAX_SELECTION = 5
        fun fromId(id: String): LearningTopic? = entries.firstOrNull { it.id == id }
    }
}
