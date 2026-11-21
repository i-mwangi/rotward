package com.hexscroll.app.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hexscroll.app.core.data.database.model.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query(
        """
        SELECT * FROM questions
        WHERE topic IN (:topics)
        ORDER BY (times_wrong > 0) DESC, last_seen_at ASC, RANDOM()
        LIMIT 1
        """
    )
    suspend fun getNextQuestionForTopics(topics: List<String>): QuestionEntity?

    @Query(
        """
        UPDATE questions
        SET times_seen = times_seen + 1,
            times_wrong = CASE WHEN :isCorrect = 1 THEN 0 ELSE times_wrong + 1 END,
            last_seen_at = :nowMillis
        WHERE id = :questionId
        """
    )
    suspend fun recordAnswer(questionId: String, isCorrect: Boolean, nowMillis: Long)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE topic = :topic")
    suspend fun countByTopic(topic: String): Int
}
