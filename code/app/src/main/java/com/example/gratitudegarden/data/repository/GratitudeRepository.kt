package com.example.gratitudegarden.data.repository

import com.example.gratitudegarden.db.GratitudeDao
import com.example.gratitudegarden.data.model.GratitudeEntry
import kotlinx.coroutines.flow.Flow

class GratitudeRepository(
    private val dao: GratitudeDao
) {

    fun getAllEntries(): Flow<List<GratitudeEntry>> {
        return dao.getAllEntries()
    }

    suspend fun insert(entry: GratitudeEntry) {
        dao.insertEntry(entry)
    }

    suspend fun update(entry: GratitudeEntry) {
        dao.updateEntry(entry)
    }

    suspend fun delete(entry: GratitudeEntry) {
        dao.deleteEntry(entry)
    }

    suspend fun getEntryById(id: Long): GratitudeEntry? {
        return dao.getEntryById(id.toInt())
    }
}