package com.example.gratitudegarden.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gratitudegarden.data.model.GratitudeEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeDao {

    @Insert
    suspend fun insertEntry(entry: GratitudeEntry)

    @Query("SELECT * FROM gratitude_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<GratitudeEntry>>
}
