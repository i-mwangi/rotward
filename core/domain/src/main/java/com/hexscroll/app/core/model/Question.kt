package com.hexscroll.app.core.model

data class Question(
    val id: String,
    val topic: LearningTopic,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)
