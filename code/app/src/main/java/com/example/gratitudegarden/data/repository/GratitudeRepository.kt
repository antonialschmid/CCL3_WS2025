package com.example.gratitudegarden.data.repository

import com.example.gratitudegarden.db.GratitudeDao
import com.example.gratitudegarden.data.model.GratitudeEntry

class GratitudeRepository(
    private val dao: GratitudeDao
) {

    suspend fun addEntry(entry: GratitudeEntry) {
        dao.insertEntry(entry)
    }

    fun getEntries() = dao.getAllEntries()

    suspend fun getEntryById(id: Int): GratitudeEntry? {
        return dao.getEntryById(id)
    }

    suspend fun updateEntry(entry: GratitudeEntry) {
        dao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: GratitudeEntry) {
        dao.deleteEntry(entry)
    }
}
