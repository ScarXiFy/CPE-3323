package com.usc.cems.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert

@Dao
interface PlaceholderDao {
    @Query("SELECT * FROM placeholder_table")
    suspend fun getAll(): List<PlaceholderEntity>

    @Insert
    suspend fun insert(entity: PlaceholderEntity): Long
}

@Database(entities = [PlaceholderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeholderDao(): PlaceholderDao
}
