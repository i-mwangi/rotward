package com.hexscroll.app.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    indices = [Index(value = ["topic"])],
)
data class QuestionEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "topic")
    val topic: String,

    @ColumnInfo(name = "prompt")
    val prompt: String,

    @ColumnInfo(name = "options_json")
    val optionsJson: String,

    @ColumnInfo(name = "correct_index")
    val correctIndex: Int,

    @ColumnInfo(name = "explanation")
    val explanation: String,

    @ColumnInfo(name = "times_seen")
    val timesSeen: Int = 0,

    @ColumnInfo(name = "times_wrong")
    val timesWrong: Int = 0,

    @ColumnInfo(name = "last_seen_at")
    val lastSeenAt: Long = 0L,
)
