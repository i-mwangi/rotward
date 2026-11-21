package com.hexscroll.app.core.di

import com.hexscroll.app.core.blocking.BlockingManager
import com.hexscroll.app.core.blocking.BlockingManagerImpl
import com.hexscroll.app.core.blocking.LearningGateCoordinator
import com.hexscroll.app.core.blocking.LearningGateCoordinatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainDiModule {

    @Binds
    @Singleton
    abstract fun bindBlockingManager(impl: BlockingManagerImpl): BlockingManager

    @Binds
    @Singleton
    abstract fun bindLearningGateCoordinator(impl: LearningGateCoordinatorImpl): LearningGateCoordinator
}
