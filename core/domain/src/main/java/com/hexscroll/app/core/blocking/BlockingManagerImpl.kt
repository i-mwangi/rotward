package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.blocking.handler.BlockAllBlockHandler
import com.hexscroll.app.core.blocking.handler.DayLimitBlockHandler
import com.hexscroll.app.core.blocking.handler.IntervalTimerBlockHandler
import com.hexscroll.app.core.blocking.handler.NoBlockHandler
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.BlockingResult
import com.hexscroll.app.core.repository.UserSettingsStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockingManagerImpl @Inject constructor(
    private val userSettingsStore: UserSettingsStore,
) : BlockingManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var activeHandler: BlockOptionHandler = NoBlockHandler()

    init {
        scope.launch {
            userSettingsStore.getBlockOptionFlow().collectLatest { option ->
                activeHandler = when (option) {
                    BlockOption.NothingSelected -> NoBlockHandler()
                    BlockOption.BlockAll -> BlockAllBlockHandler()
                    BlockOption.DailyLimit -> DayLimitBlockHandler(userSettingsStore)
                    BlockOption.IntervalTimer -> IntervalTimerBlockHandler(userSettingsStore)
                }
            }
        }
    }

    override suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult =
        activeHandler.onPeriodicCheck(elapsedSessionMillis, nowMillis)

    override suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long) =
        activeHandler.onExitBlockedContent(sessionMillis, nowMillis)
}
