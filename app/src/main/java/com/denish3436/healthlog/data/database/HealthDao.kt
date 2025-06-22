package com.denish3436.healthlog.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {
    @Query("SELECT * FROM health_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<HealthEntry>>

    @Query("SELECT * FROM health_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryByDate(date: String): HealthEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: HealthEntry)

    @Update
    suspend fun updateEntry(entry: HealthEntry)

    @Delete
    suspend fun deleteEntry(entry: HealthEntry)

    @Query("SELECT COUNT(*) FROM health_entries")
    suspend fun getEntryCount(): Int
}