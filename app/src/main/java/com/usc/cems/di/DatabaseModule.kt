package com.usc.cems.di

import android.content.Context
import androidx.room.Room
import com.usc.cems.data.local.AppDatabase
import com.usc.cems.data.local.PlaceholderDao
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cems_database"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    @Singleton
    fun providePlaceholderDao(database: AppDatabase): PlaceholderDao {
        return database.placeholderDao()
    }
}
