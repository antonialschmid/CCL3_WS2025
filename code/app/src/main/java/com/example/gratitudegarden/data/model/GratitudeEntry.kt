package com.example.gratitudegarden.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gratitude_entries")
data class GratitudeEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val mood: String,
    val timestamp: Long
)
