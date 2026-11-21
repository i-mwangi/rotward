package com.hexscroll.app.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hexscroll.app.core.data.database.model.SessionSegmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionSegmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(segment: SessionSegmentEntity): Long

    @Query("SELECT * FROM session_segments WHERE start_date_time >= :startOfDay ORDER BY start_date_time DESC")
    fun getSegmentsSince(startOfDay: String): Flow<List<SessionSegmentEntity>>
}
