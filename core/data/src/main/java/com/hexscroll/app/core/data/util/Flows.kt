package com.hexscroll.app.core.data.util

import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class HomeFlowData(
    val blockOption: BlockOption,
    val timeLimit: Long,
    val timerOverlayEnabled: Boolean,
    val pauseUntil: Long,
    val learningGateEnabled: Boolean,
    val selectedTopics: Set<LearningTopic>,
)

fun combineHomeFlows(
    blockOptionFlow: Flow<BlockOption>,
    timeLimitFlow: Flow<Long>,
    timerOverlayEnabledFlow: Flow<Boolean>,
    pauseUntilFlow: Flow<Long>,
    learningGateEnabledFlow: Flow<Boolean>,
    selectedTopicsFlow: Flow<Set<LearningTopic>>,
): Flow<HomeFlowData> = combine(
    blockOptionFlow,
    timeLimitFlow,
    timerOverlayEnabledFlow,
    pauseUntilFlow,
    learningGateEnabledFlow,
    selectedTopicsFlow,
) { values ->
    @Suppress("UNCHECKED_CAST")
    HomeFlowData(
        blockOption = values[0] as BlockOption,
        timeLimit = values[1] as Long,
        timerOverlayEnabled = values[2] as Boolean,
        pauseUntil = values[3] as Long,
        learningGateEnabled = values[4] as Boolean,
        selectedTopics = values[5] as Set<LearningTopic>,
    )
}
