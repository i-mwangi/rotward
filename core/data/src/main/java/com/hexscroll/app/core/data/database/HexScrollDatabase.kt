package com.hexscroll.app.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hexscroll.app.core.data.database.dao.QuestionDao
import com.hexscroll.app.core.data.database.dao.SessionSegmentDao
import com.hexscroll.app.core.data.database.dao.UserSettingsDao
import com.hexscroll.app.core.data.database.model.QuestionEntity
import com.hexscroll.app.core.data.database.model.SessionSegmentEntity
import com.hexscroll.app.core.data.database.model.UserSettingsEntity

@Database(
    entities = [
        UserSettingsEntity::class,
        SessionSegmentEntity::class,
        QuestionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class HexScrollDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun sessionSegmentDao(): SessionSegmentDao
    abstract fun questionDao(): QuestionDao
}
