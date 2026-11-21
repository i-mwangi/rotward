package com.hexscroll.app.core.data.di

import android.content.Context
import androidx.room.Room
import com.hexscroll.app.core.data.database.HexScrollDatabase
import com.hexscroll.app.core.data.database.dao.QuestionDao
import com.hexscroll.app.core.data.database.dao.SessionSegmentDao
import com.hexscroll.app.core.data.database.dao.UserSettingsDao
import com.hexscroll.app.core.data.repository.QuestionRepositoryImpl
import com.hexscroll.app.core.data.repository.SessionSegmentStoreImpl
import com.hexscroll.app.core.data.repository.SessionTrackerImpl
import com.hexscroll.app.core.data.repository.UserSettingsStoreImpl
import com.hexscroll.app.core.repository.QuestionRepository
import com.hexscroll.app.core.repository.SessionSegmentStore
import com.hexscroll.app.core.repository.SessionTracker
import com.hexscroll.app.core.repository.UserSettingsStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiProvideModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HexScrollDatabase =
        Room.databaseBuilder(context, HexScrollDatabase::class.java, "hexscroll.db")
            .build()

    @Provides
    @Singleton
    fun provideUserSettingsDao(db: HexScrollDatabase): UserSettingsDao = db.userSettingsDao()

    @Provides
    @Singleton
    fun provideSessionSegmentDao(db: HexScrollDatabase): SessionSegmentDao = db.sessionSegmentDao()

    @Provides
    @Singleton
    fun provideQuestionDao(db: HexScrollDatabase): QuestionDao = db.questionDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataDiBindModule {

    @Binds
    @Singleton
    abstract fun bindUserSettingsStore(impl: UserSettingsStoreImpl): UserSettingsStore

    @Binds
    @Singleton
    abstract fun bindSessionTracker(impl: SessionTrackerImpl): SessionTracker

    @Binds
    @Singleton
    abstract fun bindSessionSegmentStore(impl: SessionSegmentStoreImpl): SessionSegmentStore

    @Binds
    @Singleton
    abstract fun bindQuestionRepository(impl: QuestionRepositoryImpl): QuestionRepository
}
