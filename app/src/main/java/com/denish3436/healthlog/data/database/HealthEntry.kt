package com.denish3436.healthlog.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "health_entries")
@Parcelize
data class HealthEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: String,
    val waterIntake: Int = 0, // in glasses
    val sleepHours: Float = 0f,
    val mood: String = "Neutral", // Happy, Neutral, Sad
    val exerciseMinutes: Int = 0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable