package com.arushlab.android.di

import android.content.Context
import androidx.room.Room
import com.arushlab.android.local.ArushLabDatabase
import com.arushlab.android.local.TestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArushLabDatabase {
        return Room.databaseBuilder(
            context,
            ArushLabDatabase::class.java,
            "arushlab_db"
        ).build()
    }

    @Provides
    fun provideTestDao(database: ArushLabDatabase): TestDao {
        return database.testDao()
    }
}
