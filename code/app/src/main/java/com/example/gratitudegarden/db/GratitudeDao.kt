package com.example.gratitudegarden.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gratitudegarden.data.model.GratitudeEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeDao {

    @Insert
    suspend fun insertEntry(entry: GratitudeEntry)

    @Query("SELECT * FROM gratitude_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<GratitudeEntry>>

    @Query("SELECT * FROM gratitude_entries WHERE id = :id")
    suspend fun getEntryById(id: Int): GratitudeEntry?

    @Update
    suspend fun updateEntry(entry: GratitudeEntry)

    @Delete
    suspend fun deleteEntry(entry: GratitudeEntry)

}
