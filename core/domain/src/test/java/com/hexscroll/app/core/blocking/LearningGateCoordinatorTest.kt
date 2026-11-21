package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.FakeQuestionRepository
import com.hexscroll.app.core.FakeUserSettingsStore
import com.hexscroll.app.core.model.LearningGateDecision
import com.hexscroll.app.core.model.LearningTopic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LearningGateCoordinatorTest {
    private lateinit var store: FakeUserSettingsStore
    private lateinit var repo: FakeQuestionRepository
    private lateinit var coordinator: LearningGateCoordinatorImpl

    @Before
    fun setUp() {
        store = FakeUserSettingsStore().apply {
            learningGateEnabled = true
            selectedTopics = setOf(LearningTopic.PROGRAMMING, LearningTopic.MATH)
            hauntCooldownUntil = 0L
            hauntCooldownMillis = 180_000L
            quizRewardMillis = 300_000L
        }
        repo = FakeQuestionRepository()
        coordinator = LearningGateCoordinatorImpl(store, repo)
    }

    @Test
    fun `returns PassThrough when learning gate disabled`() = runTest {
        store.learningGateEnabled = false
        val result = coordinator.evaluateBlock(nowMillis = 1_000L)
        assertTrue(result is LearningGateDecision.PassThrough)
    }

    @Test
    fun `returns ShowHauntLockout when inside cooldown window`() = runTest {
        val now = 1_000_000L
        store.hauntCooldownUntil = now + 90_000L
        val result = coordinator.evaluateBlock(nowMillis = now)
        assertTrue(result is LearningGateDecision.ShowHauntLockout)
        assertEquals(90_000L, (result as LearningGateDecision.ShowHauntLockout).remainingMillis)
    }

    @Test
    fun `returns PassThrough when selected topics is empty`() = runTest {
        store.selectedTopics = emptySet()
        val result = coordinator.evaluateBlock(nowMillis = 1_000L)
        assertTrue(result is LearningGateDecision.PassThrough)
    }

    @Test
    fun `returns PassThrough when no question available`() = runTest {
        repo.nextQuestion = null
        val result = coordinator.evaluateBlock(nowMillis = 1_000L)
        assertTrue(result is LearningGateDecision.PassThrough)
    }

    @Test
    fun `returns ShowQuiz when gate enabled and question available`() = runTest {
        val result = coordinator.evaluateBlock(nowMillis = 1_000L)
        assertTrue(result is LearningGateDecision.ShowQuiz)
        assertEquals("test_001", (result as LearningGateDecision.ShowQuiz).question.id)
    }

    @Test
    fun `onQuizPassed sets pause until and returns correct timestamp`() = runTest {
        store.quizRewardMillis = 300_000L
        val now = 1_000_000L
        val pauseUntil = coordinator.onQuizPassed("test_001", nowMillis = now)
        assertEquals(now + 300_000L, pauseUntil)
        assertEquals(now + 300_000L, store.pauseUntil)
    }

    @Test
    fun `onQuizPassed records correct answer in repository`() = runTest {
        coordinator.onQuizPassed("test_001", nowMillis = 1_000_000L)
        val recorded = repo.answers.first()
        assertEquals("test_001", recorded.first)
        assertTrue(recorded.second) // isCorrect
    }

    @Test
    fun `onQuizFailed sets haunt cooldown and returns correct timestamp`() = runTest {
        store.hauntCooldownMillis = 180_000L
        val now = 1_000_000L
        val cooldownUntil = coordinator.onQuizFailed("test_001", nowMillis = now)
        assertEquals(now + 180_000L, cooldownUntil)
        assertEquals(now + 180_000L, store.hauntCooldownUntil)
    }

    @Test
    fun `onQuizFailed records wrong answer in repository`() = runTest {
        coordinator.onQuizFailed("test_001", nowMillis = 1_000_000L)
        val recorded = repo.answers.first()
        assertEquals("test_001", recorded.first)
        assertTrue(!recorded.second) // isCorrect == false
    }

    @Test
    fun `cooldown lockout returns PassThrough immediately after cooldown expires`() = runTest {
        val now = 1_000_000L
        store.hauntCooldownUntil = now - 1L // expired 1ms ago
        val result = coordinator.evaluateBlock(nowMillis = now)
        // should NOT be a lockout, should be ShowQuiz since gate is enabled and question available
        assertTrue(result is LearningGateDecision.ShowQuiz)
    }

    @Test
    fun `ensureSeeded is called during evaluateBlock`() = runTest {
        coordinator.evaluateBlock(nowMillis = 1_000L)
        assertTrue(repo.seededCount >= 1)
    }
}
