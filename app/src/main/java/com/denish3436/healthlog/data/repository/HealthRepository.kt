package com.denish3436.healthlog.data.repository

import com.denish3436.healthlog.data.database.HealthDao
import com.denish3436.healthlog.data.database.HealthEntry
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val healthDao: HealthDao) {

    fun getAllEntries(): Flow<List<HealthEntry>> = healthDao.getAllEntries()

    suspend fun getEntryByDate(date: String): HealthEntry? = healthDao.getEntryByDate(date)

    suspend fun insertEntry(entry: HealthEntry) = healthDao.insertEntry(entry)

    suspend fun updateEntry(entry: HealthEntry) = healthDao.updateEntry(entry)

    suspend fun deleteEntry(entry: HealthEntry) = healthDao.deleteEntry(entry)

    suspend fun getEntryCount(): Int = healthDao.getEntryCount()
}