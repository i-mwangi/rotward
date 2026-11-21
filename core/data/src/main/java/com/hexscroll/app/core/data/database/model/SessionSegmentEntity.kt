package com.hexscroll.app.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "session_segments",
    indices = [
        Index(value = ["start_date_time"]),
        Index(value = ["app", "start_date_time"]),
    ],
)
data class SessionSegmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "app")
    val app: String,

    @ColumnInfo(name = "duration_millis")
    val durationMillis: Long,

    @ColumnInfo(name = "start_date_time")
    val startDateTime: LocalDateTime,
)
