package com.hexscroll.app.core.repository

import com.hexscroll.app.core.model.SessionSegment
import kotlinx.coroutines.flow.Flow

interface SessionSegmentStore {
    suspend fun insertSegment(segment: SessionSegment)
    fun getSegmentsForToday(): Flow<List<SessionSegment>>
}
