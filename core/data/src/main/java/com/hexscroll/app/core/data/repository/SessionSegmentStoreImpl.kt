package com.hexscroll.app.core.data.repository

import com.hexscroll.app.core.data.database.dao.SessionSegmentDao
import com.hexscroll.app.core.data.database.model.SessionSegmentEntity
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.SessionSegment
import com.hexscroll.app.core.repository.SessionSegmentStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionSegmentStoreImpl @Inject constructor(
    private val dao: SessionSegmentDao,
) : SessionSegmentStore {

    override suspend fun insertSegment(segment: SessionSegment) {
        dao.insert(
            SessionSegmentEntity(
                app = segment.app.name,
                durationMillis = segment.durationMillis,
                startDateTime = segment.startDateTime,
            )
        )
    }

    override fun getSegmentsForToday(): Flow<List<SessionSegment>> {
        val startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toString()
        return dao.getSegmentsSince(startOfDay).map { entities ->
            entities.map { entity ->
                SessionSegment(
                    id = entity.id,
                    app = BlockableApp.valueOf(entity.app),
                    durationMillis = entity.durationMillis,
                    startDateTime = entity.startDateTime,
                )
            }
        }
    }
}
